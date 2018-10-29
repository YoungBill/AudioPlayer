package playaudio.android.com.bean;

import java.util.List;

/**
 * Created by taochen on 2018/10/25.
 */

public class RadioResponse {


    /**
     * data : {"items":[{"title":"Radio Mirchi 98.3 FM","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012034819/428319.png","frequency":"98.3 FM","streams":[{"url":"http://peridot.streamguys.com:7150/Mirchi","mediaType":"AAC"},{"url":"http://peridot.streamguys.com:7150/Mirchi.m3u","mediaType":"AAC"}],"id":1558},{"title":"Indian Hindi Desi Bollywood Evergreen Hits","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012040926/781497.png","frequency":"999.10 FM","streams":[{"url":"http://192.240.102.133:11454/stream/1/","mediaType":"MP3"},{"url":"http://192.240.102.133:11454/listen.pls?sid=1","mediaType":"MP3"}],"id":14255},{"title":"Bollywood Radio and Beyond","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012061403/87976.png","frequency":"999.11 FM","streams":[{"url":"http://96.31.83.86:8084/","mediaType":"MP3"},{"url":"http://96.31.83.86:8084/listen.pls","mediaType":"MP3"}],"id":15842},{"title":"Red FM 93.5","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012061554/161875.png","frequency":"93.5 FM","streams":[{"url":"https://playerservices.streamtheworld.com/pls/CKYRFM.pls","mediaType":"MP3"},{"url":"https://playerservices.streamtheworld.com/api/livestream-redirect/CKYRFM_SC","mediaType":"MP3"}],"id":13784},{"title":"City 101.6 FM","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062001/631833.png","frequency":"101.6 FM","streams":[{"url":"https://playerservices.streamtheworld.com/api/livestream-redirect/ARNCITY_SC","mediaType":"MP3"},{"url":"https://playerservices.streamtheworld.com/pls/ARNCITY.pls","mediaType":"MP3"}],"id":1535},{"title":"Planet Radio City Tamil","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062233/686250.png","frequency":"91.1 FM","streams":[{"url":"http://prclive1.listenon.in:9948/","mediaType":"MP3"},{"url":"http://prclive1.listenon.in:9948/listen.pls","mediaType":"MP3"}],"id":18440},{"title":"All India Radio FM Gold","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062425/32404.png","frequency":"102.3 FM","streams":[{"url":"https://airfmgold-lh.akamaihd.net/i/fmgold_1@507591/master.m3u8","mediaType":"HLS"}],"id":17283},{"title":"Shakti FM","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062632/245097.png","frequency":"92.7 FM","streams":[{"url":"http://live.trusl.com:1160/stream/1/","mediaType":"AAC"},{"url":"http://live.trusl.com:1160/listen.pls?sid=1","mediaType":"AAC"}],"id":1541}],"code":100}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * items : [{"title":"Radio Mirchi 98.3 FM","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012034819/428319.png","frequency":"98.3 FM","streams":[{"url":"http://peridot.streamguys.com:7150/Mirchi","mediaType":"AAC"},{"url":"http://peridot.streamguys.com:7150/Mirchi.m3u","mediaType":"AAC"}],"id":1558},{"title":"Indian Hindi Desi Bollywood Evergreen Hits","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012040926/781497.png","frequency":"999.10 FM","streams":[{"url":"http://192.240.102.133:11454/stream/1/","mediaType":"MP3"},{"url":"http://192.240.102.133:11454/listen.pls?sid=1","mediaType":"MP3"}],"id":14255},{"title":"Bollywood Radio and Beyond","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012061403/87976.png","frequency":"999.11 FM","streams":[{"url":"http://96.31.83.86:8084/","mediaType":"MP3"},{"url":"http://96.31.83.86:8084/listen.pls","mediaType":"MP3"}],"id":15842},{"title":"Red FM 93.5","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012061554/161875.png","frequency":"93.5 FM","streams":[{"url":"https://playerservices.streamtheworld.com/pls/CKYRFM.pls","mediaType":"MP3"},{"url":"https://playerservices.streamtheworld.com/api/livestream-redirect/CKYRFM_SC","mediaType":"MP3"}],"id":13784},{"title":"City 101.6 FM","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062001/631833.png","frequency":"101.6 FM","streams":[{"url":"https://playerservices.streamtheworld.com/api/livestream-redirect/ARNCITY_SC","mediaType":"MP3"},{"url":"https://playerservices.streamtheworld.com/pls/ARNCITY.pls","mediaType":"MP3"}],"id":1535},{"title":"Planet Radio City Tamil","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062233/686250.png","frequency":"91.1 FM","streams":[{"url":"http://prclive1.listenon.in:9948/","mediaType":"MP3"},{"url":"http://prclive1.listenon.in:9948/listen.pls","mediaType":"MP3"}],"id":18440},{"title":"All India Radio FM Gold","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062425/32404.png","frequency":"102.3 FM","streams":[{"url":"https://airfmgold-lh.akamaihd.net/i/fmgold_1@507591/master.m3u8","mediaType":"HLS"}],"id":17283},{"title":"Shakti FM","iconUrl":"http://s.c-launcher.com/upload/app/radio/20181012062632/245097.png","frequency":"92.7 FM","streams":[{"url":"http://live.trusl.com:1160/stream/1/","mediaType":"AAC"},{"url":"http://live.trusl.com:1160/listen.pls?sid=1","mediaType":"AAC"}],"id":1541}]
         * code : 100
         */

        private int code;
        private List<ItemsBean> items;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * title : Radio Mirchi 98.3 FM
             * iconUrl : http://s.c-launcher.com/upload/app/radio/20181012034819/428319.png
             * frequency : 98.3 FM
             * streams : [{"url":"http://peridot.streamguys.com:7150/Mirchi","mediaType":"AAC"},{"url":"http://peridot.streamguys.com:7150/Mirchi.m3u","mediaType":"AAC"}]
             * id : 1558
             */

            private String title;
            private String iconUrl;
            private String frequency;
            private int id;
            private List<StreamsBean> streams;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getFrequency() {
                return frequency;
            }

            public void setFrequency(String frequency) {
                this.frequency = frequency;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public List<StreamsBean> getStreams() {
                return streams;
            }

            public void setStreams(List<StreamsBean> streams) {
                this.streams = streams;
            }

            public static class StreamsBean {
                /**
                 * url : http://peridot.streamguys.com:7150/Mirchi
                 * mediaType : AAC
                 */

                private String url;
                private String mediaType;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getMediaType() {
                    return mediaType;
                }

                public void setMediaType(String mediaType) {
                    this.mediaType = mediaType;
                }
            }
        }
    }
}
