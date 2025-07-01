package in.woloo.www.giftcard.model;


public class GiftCardModelResponse {
    private final Data data;

    public GiftCardModelResponse(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private final String status;

        private final String message;

        private final GiftRecieved giftRecieved;

        private final GiftSent giftSent;

        public Data(String status, String message, GiftRecieved giftRecieved, GiftSent giftSent) {
            this.status = status;
            this.message = message;
            this.giftRecieved = giftRecieved;
            this.giftSent = giftSent;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public GiftRecieved getGiftRecieved() {
            return giftRecieved;
        }

        public GiftSent getGiftSent() {
            return giftSent;
        }

        public static class GiftRecieved {
            private final int userId;

            private final String transactionType;

            private final String remarks;

            private final int value;

            private final String type;

            private final int isGift;

            private final String updatedAt;

            private final String createdAt;

            private final int id;

            public GiftRecieved(int userId, String transactionType, String remarks, int value,
                                String type, int isGift, String updatedAt, String createdAt, int id) {
                this.userId = userId;
                this.transactionType = transactionType;
                this.remarks = remarks;
                this.value = value;
                this.type = type;
                this.isGift = isGift;
                this.updatedAt = updatedAt;
                this.createdAt = createdAt;
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public String getTransactionType() {
                return transactionType;
            }

            public String getRemarks() {
                return remarks;
            }

            public int getValue() {
                return value;
            }

            public String getType() {
                return type;
            }

            public int getIsGift() {
                return isGift;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public int getId() {
                return id;
            }
        }

        public static class GiftSent {
            private final int userId;

            private final String transactionType;

            private final String remarks;

            private final int value;

            private final String type;

            private final int isGift;

            private final String updatedAt;

            private final String createdAt;

            private final int id;

            public GiftSent(int userId, String transactionType, String remarks, int value,
                            String type, int isGift, String updatedAt, String createdAt, int id) {
                this.userId = userId;
                this.transactionType = transactionType;
                this.remarks = remarks;
                this.value = value;
                this.type = type;
                this.isGift = isGift;
                this.updatedAt = updatedAt;
                this.createdAt = createdAt;
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public String getTransactionType() {
                return transactionType;
            }

            public String getRemarks() {
                return remarks;
            }

            public int getValue() {
                return value;
            }

            public String getType() {
                return type;
            }

            public int getIsGift() {
                return isGift;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public int getId() {
                return id;
            }
        }
    }
}
