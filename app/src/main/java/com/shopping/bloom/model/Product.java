package com.shopping.bloom.model;

import java.util.List;

public class Product {
    public int id;
    public Object parent_id;
    public String category_name;
    public String category_thumbnail;
    public String is_bigthumbnail_show;
    public Object big_thumbnail;
    public String type;
    public Object created_at;
    public Object updated_at;
    public List<SubProduct> sub_category;
}
