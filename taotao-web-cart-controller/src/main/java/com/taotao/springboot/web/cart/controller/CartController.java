package com.taotao.springboot.web.cart.controller;

import com.taotao.springboot.item.domain.pojo.TbItem;
import com.taotao.springboot.item.domain.result.TaotaoResult;
import com.taotao.springboot.item.export.ItemResource;
import com.taotao.springboot.web.cart.common.utils.CookieUtils;
import com.taotao.springboot.web.cart.common.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: CartController</p>
 * <p>Description: 购物车管理Controller</p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-06 21:01</p>
 * @author ChengTengfei
 * @version 1.0
 */
@Controller
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Value("${CART_KEY}")
    private String CART_KEY;

    @Value("${CART_EXPIER}")
    private Integer CART_EXPIER;

    @Autowired
    private ItemResource itemResource;

    /**
     * 购物车添加商品
     * @param itemId    商品ID
     * @param num       数量
     */
    @RequestMapping("/addToCart/{itemId}")
    public String addItemCart(@PathVariable Long itemId,
                              @RequestParam(defaultValue="1")Integer num, HttpServletRequest request,
                              HttpServletResponse response) {
        log.info("购物车添加商品, itemId={} & num={}", String.valueOf(itemId), String.valueOf(num));
        // #1 获取购物车商品列表
        List<TbItem> cartItemList = getCartItemList(request);
        // #2 判断商品在购物车中是否存在
        boolean flag = false;
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 若存在，则数量相加
                tbItem.setNum(tbItem.getNum() + num);
                flag = true;
                break;
            }
        }
        // #3 若不存在，则添加一个新的商品
        if (!flag) {
            // #3.1 调用服务，获取商品详情信息
            TbItem tbItem = itemResource.getItemById(itemId);
            // #3.2 设置数量
            tbItem.setNum(num);
            // #3.3 设置logo
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)) {
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }
            // #3.4 添加
            cartItemList.add(tbItem);
        }
        // #4 购物车列表写入Cookie
        String jsonString = JacksonUtils.objectToJson(cartItemList);
        CookieUtils.setCookie(request, response, CART_KEY, jsonString, CART_EXPIER, true);
        log.info("购物车添加商品, res={}", jsonString);
        // #5 返回添加成功页面
        return "cartSuccess";
    }

    private List<TbItem> getCartItemList(HttpServletRequest request) {
        // #1 从Cookie中，获取购物车商品列表
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        return JacksonUtils.jsonToList(json, TbItem.class);
    }

    /**
     * 获取购物车商品列表
     */
    @RequestMapping("/cart")
    public String showCartList(HttpServletRequest request) {
        log.info("获取购物车商品列表");
        List<TbItem> cartItemList = getCartItemList(request);
        log.info("获取购物车商品列表, res={}", JacksonUtils.objectToJson(cartItemList));
        request.setAttribute("cartList", cartItemList);
        return "cart";
    }

    /**
     * 修改购物车中某商品数量
     * @param itemId    商品ID
     * @param num       数量
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemNum(@PathVariable Long itemId, @PathVariable Integer num,
                                      HttpServletRequest request, HttpServletResponse response) {
        log.info("修改购物车中某商品数量, itemId={} & num={}", String.valueOf(itemId), String.valueOf(num));
        // #1 获取购物车列表
        List<TbItem> cartList = getCartItemList(request);
        // #2 查询对应商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 更新数量
                tbItem.setNum(num);
                break;
            }
        }
        // #3 写入Cookie
        String jsonString = JacksonUtils.objectToJson(cartList);
        CookieUtils.setCookie(request, response, CART_KEY, jsonString, CART_EXPIER, true);
        log.info("购物车添加商品, res={}", jsonString);
        return TaotaoResult.ok();
    }

    /**
     * 删除购物车某商品
     * @param itemId    商品ID
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,
                                 HttpServletRequest request ,
                                 HttpServletResponse response) {
        log.info("删除购物车某商品, itemId={}", String.valueOf(itemId));
        // #1 获取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        // #2 查询对应商品
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 删除商品
                cartItemList.remove(tbItem);
                break;
            }
        }
        // #3 写入Cookie
        String jsonString = JacksonUtils.objectToJson(cartItemList);
        CookieUtils.setCookie(request, response, CART_KEY, jsonString, CART_EXPIER, true);
        log.info("购物车添加商品, res={}", jsonString);
        // #4 重定向，购物车列表页面
        return "redirect:/cart";
    }

}

