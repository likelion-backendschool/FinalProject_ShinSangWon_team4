package com.ll.ebooks.domain.order.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.mybook.service.MyBookService;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.entity.OrderStatus;
import com.ll.ebooks.domain.order.exception.NotEnoughMoneyException;
import com.ll.ebooks.domain.order.exception.NotMatchedOrderIdException;
import com.ll.ebooks.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

//TODO 예외 처리를 묶어서 해야할까 ..? 컨트롤러 / 서비스 나눠서 해야할까 ... 아니면 지금처럼 .. ? ?
@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final MyBookService myBookService;


    @GetMapping("/list")
    public String showOrderList(Model model, Principal principal) {

        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        model.addAttribute("orderList", orderService.findAllByMemberId(loginMember.getId()));

        return "/order/list";
    }

    //장바구니 전체 주문
    @GetMapping("/create")
    public String makeAllCartItemOrder(Principal principal) {
        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        Order order = orderService.createFromCart(loginMember);

        return "redirect:/order/%d".formatted(order.getId());
    }

    //장바구니 단일 주문
    @GetMapping("/create/{id}")
    public String makeSingleCartItemOrder(@PathVariable Long id, Principal principal) {
        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        Order order = orderService.createSingleOrder(id, loginMember);

        return "redirect:/order/%d".formatted(order.getId());
    }

    //주문 취소
    @GetMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, Principal principal) {

        Order order = orderService.findById(id);

        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        if(!order.getMember().equals(loginMember)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비정상적인 접근입니다.");
        }

        orderService.cancel(order);

        return "redirect:/order/%d".formatted(order.getId());
    }

    //주문 환불
    @GetMapping("{id}/refund")
    public String refundOrder(@PathVariable Long id, Principal principal) {

        Order order = orderService.findById(id);

        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        if(!order.getMember().equals(loginMember)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비정상적인 접근입니다.");
        }

        if(!order.getOrderStatus().equals(OrderStatus.PAID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제가 되지 않은 요청에 대해서는 환불할 수 없습니다.");
        }

        orderService.refund(order);
        //환불 시 mybook에서 제거됨
        myBookService.deRegisteringBook(loginMember, order);
        return "redirect:/order/%d".formatted(order.getId());
    }


    @GetMapping("/{id}")
    public String showOrderDetail(@PathVariable Long id, Principal principal, Model model) {

        Order order = orderService.findById(id);

        if(order == null) {
            throw new NoSuchElementException("해당 주문이 존재하지 않습니다.");
        }

        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        if(!order.getMember().equals(loginMember)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인의 주문만 볼 수 있습니다.");
        }

        int restCash = memberService.getRestCash(loginMember);
        //환불 가능여부(기본 값은 불가능)
        boolean isRefundable = false;
        //만약, 결제 된 상태라면 환불 가능 여부 판단
        if(order.getOrderStatus() == OrderStatus.PAID) {
            isRefundable = orderService.getRefundPossibility(order);
        }

        model.addAttribute("isRefundable", isRefundable);
        model.addAttribute("order", order);
        model.addAttribute("memberRestCash", restCash);
        return "order/detail";
    }

    @PostMapping("/{id}/payByRestCashOnly")
    public String restCashOnlyForm(@PathVariable Long id, Principal principal, Model model) {
        Order order = orderService.findById(id);

        if(order == null) {
            throw new NoSuchElementException("해당 주문이 존재하지 않습니다.");
        }

        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        if(!order.getMember().equals(loginMember)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인의 주문만 볼 수 있습니다.");
        }

        orderService.payOnlyRestCash(order);
        //mybook 등록
        myBookService.registeringBook(loginMember, order);
        return "redirect:/order/%d".formatted(id);

    }


    // --- 토스페이먼츠 api 연동 시작 ---

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    private final String SECRET_KEY = "test_sk_7XZYkKL4MrjE2XEXvEWV0zJwlEWR";

    @RequestMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable Long id,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model, Principal principal) throws Exception {

        Order order = orderService.findById(id);
        long paymentOrderId = Long.parseLong(orderId.split("__")[1]);
        // id값이 맞지 않을 때
        if( id != paymentOrderId) {
            throw new NotMatchedOrderIdException();
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        Member member = memberService.findByUsername(principal.getName()).orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));
        int restCash = memberService.getRestCash(member);
        int restPayCash = (int) (order.getTotalPayPrice() - amount);

        if(restPayCash > restCash) {
            throw new NotEnoughMoneyException();
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            //결제 로직 구현
            orderService.payTossPayments(order, restPayCash);
            //mybook 등록
            myBookService.registeringBook(member, order);
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
            return "redirect:/order/%d".formatted(order.getId());
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }

}
