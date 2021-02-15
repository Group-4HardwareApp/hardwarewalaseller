package com.e.hardwaremallseller.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductName {

        @SerializedName("productNameId")
        @Expose
        private String productNameId;
        @SerializedName("categoryId")
        @Expose
        private String categoryId;
        @SerializedName("productName")
        @Expose
        private String productName;

        public String getProductNameId() {
            return productNameId;
        }

        public void setProductNameId(String productNameId) {
            this.productNameId = productNameId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }



}
