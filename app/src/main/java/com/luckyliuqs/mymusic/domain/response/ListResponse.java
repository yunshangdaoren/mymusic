package com.luckyliuqs.mymusic.domain.response;

import java.util.List;

public class ListResponse<T> extends BaseResponse{
    private List<T> data;

    private Meta meta;

    public List<T> getData() {
        return data;
    }

    public ListResponse setData(List<T> data) {
        this.data = data;
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public ListResponse setMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public static class Meta{
        /**
         * 当前页数
         */
        private int current_page;

        /**
         * 总页数
         */
        private int total_pages;

        /**
         * 总数量
         */
        private int total_count;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        /**
         * @return 当前页数
         */
        public int getCurrent_page() {
            return current_page;
        }

        /**
         * 设置当前页数
         * @param current_page
         * @return
         */
        public Meta setCurrent_page(int current_page) {
            this.current_page = current_page;
            return this;
        }

        /**
         * @return 总页数
         */
        public int getTotal_pages() {
            return total_pages;
        }

        /**
         * 设置总页数
         * @param total_pages
         * @return
         */
        public Meta setTotal_pages(int total_pages) {
            this.total_pages = total_pages;
            return this;
        }

        /**
         * 开始页数是1
         * @param page
         * @return 下一页
         */
        public static int nextPage(Meta page) {
            if (page != null) {
                return page.getCurrent_page()+1;
            }
            return 1;
        }
    }
}
