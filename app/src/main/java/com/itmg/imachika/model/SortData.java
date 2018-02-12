package com.itmg.imachika.model;

import com.itmg.imachika.application.APP;
import com.itmg.imachika.util.Constant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class SortData implements Serializable {

    private String status;
    private ArrayList<SortInfo> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SortInfo> getData() {
        return data;
    }

    public void setData(ArrayList<SortInfo> data) {
        this.data = data;
    }

    public class SortInfo{

        private String _id;
        private SortStr text;
        private SortStr text_cn;
        private SortStr text_en;
        private String sort_type;
        private String top_category_id;
        private String category_id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public SortStr getText() {
            switch (APP.languageType){
                case Constant.LANGUAGE_TYPE_JP:
                    return this.text;
                case Constant.LANGUAGE_TYPE_CN:
                    return this.text_cn;
                case Constant.LANGUAGE_TYPE_EN:
                    return this.text_en;
                default:
                    return this.text;
            }
        }

        public void setText(SortStr text) {
            this.text = text;
        }

        public SortStr getText_cn() {
            return text_cn;
        }

        public void setText_cn(SortStr text_cn) {
            this.text_cn = text_cn;
        }

        public SortStr getText_en() {
            return text_en;
        }

        public void setText_en(SortStr text_en) {
            this.text_en = text_en;
        }

        public String getSort_type() {
            return sort_type;
        }

        public void setSort_type(String sort_type) {
            this.sort_type = sort_type;
        }

        public String getTop_category_id() {
            return top_category_id;
        }

        public void setTop_category_id(String top_category_id) {
            this.top_category_id = top_category_id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public class SortStr{

            private String sort0;
            private String sort1;
            private String sort2;
            private String sort3;
            private String sort4;
            private String sort5;

            public String getSort0() {
                return sort0;
            }

            public void setSort0(String sort0) {
                this.sort0 = sort0;
            }

            public String getSort1() {
                return sort1;
            }

            public void setSort1(String sort1) {
                this.sort1 = sort1;
            }

            public String getSort2() {
                return sort2;
            }

            public void setSort2(String sort2) {
                this.sort2 = sort2;
            }

            public String getSort3() {
                return sort3;
            }

            public void setSort3(String sort3) {
                this.sort3 = sort3;
            }

            public String getSort4() {
                return sort4;
            }

            public void setSort4(String sort4) {
                this.sort4 = sort4;
            }

            public String getSort5() {
                return sort5;
            }

            public void setSort5(String sort5) {
                this.sort5 = sort5;
            }

        }

    }
}
