package com.example.swd1.presenters;

import android.content.Context;

import com.example.swd1.models.ProductProvider;
import com.example.swd1.models.entities.Product;
import com.example.swd1.views.ProductViewListener;

import java.util.List;

public class ProductListPresenter implements ProductListPresenterListener {

    private ProductProvider provider;
    private ProductViewListener callback;

    public ProductListPresenter(ProductViewListener callback, Context context) {

        this.callback = callback;
        this.provider = new ProductProvider(this, context);
    }

    public void loadProductListByCate(int cateId) {
        provider.getListProductByCate(cateId);
    }

    public void loadProductListBySearching(String name) {
        provider.getListProductByName(name);
    }

    @Override
    public void onGetListProductSuccess(List<Product> list) {
        callback.displayProduct(list);
    }

    @Override
    public void onGetListProductFailed() {
        callback.displayError();
    }
}
