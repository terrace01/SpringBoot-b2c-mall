package cn.luxun.mall.service.impl;

import cn.luxun.mall.entity.ProductImg;
import cn.luxun.mall.entity.ShoppingCart;
import cn.luxun.mall.mapper.ShoppingCartMapper;
import cn.luxun.mall.service.ProductImgService;
import cn.luxun.mall.service.ProductService;
import cn.luxun.mall.service.ShoppingCartService;
import cn.luxun.mall.vo.ResultVo;
import cn.luxun.mall.vo.ShoppingCartVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

	private final ProductService productService;

	private final ProductImgService productImgService;

	public ShoppingCartServiceImpl(ProductService productService, ProductImgService productImgService) {
		this.productService = productService;
		this.productImgService = productImgService;
	}

	@Override
	public ResultVo getAllShoppingCart(Integer userId) {
		List<ShoppingCart> list = this.list();
		return ResultVo.success(list);
	}

	@Override
	public ResultVo saveProductToShoppingCart(ShoppingCart shoppingCart) {
		this.save(shoppingCart);
		return ResultVo.success("添加商品到购物车成功");
	}

	@Override
	public ResultVo getShoppingCartByUserId(String userId) {

		// 条件构造器
		LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

		// 添加条件
		System.out.println(userId);
		queryWrapper.eq(ShoppingCart::getUserId, userId);
		// 购物车
		List<ShoppingCart> list = this.list(queryWrapper);
		List<ShoppingCartVo> shoppingCartVoList = list.stream().map((item) -> {
			ShoppingCartVo shoppingCartVo = new ShoppingCartVo();

			// 拷贝
			BeanUtils.copyProperties(item, shoppingCartVo);

			// 获得img对象
			ProductImg productImg = productImgService.getProductImgById(item.getProductId()).get(0);
			shoppingCartVo.setProductImg(productImg);

			return shoppingCartVo;
		}).collect(Collectors.toList());

		return ResultVo.success(shoppingCartVoList);
	}
}
