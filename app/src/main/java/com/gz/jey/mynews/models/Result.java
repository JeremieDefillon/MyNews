package com.gz.jey.mynews.models;

public class Result {

        private String section;
        private String subsection;
        private String title;
        private String url;
        private String publishedDate;
        private String imageUrl;

        public String getSection() {
            return section;
        }
        public String getSubsection() {
            return subsection;
        }
        public String getTitle() {
            return title;
        }
        public String getUrl() {
            return url;
        }
        public String getPublishedDate() {
            return publishedDate;
        }
        public String getImageUrl() {
            return imageUrl;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public void setSubsection(String subsection) {
            this.subsection = subsection;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setPublishedDate(String published) {
            this.publishedDate = published;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

}
