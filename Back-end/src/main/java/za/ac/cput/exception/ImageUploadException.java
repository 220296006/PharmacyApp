package za.ac.cput.exception;
/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/04/04
 * @Time : 19:24
 **/

    public  class ImageUploadException extends RuntimeException {
        public ImageUploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
