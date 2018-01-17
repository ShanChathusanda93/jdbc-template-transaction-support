public class TransactionException extends Exception {
    public TransactionException(){
        super();
    }

    public TransactionException(String errorMsg,Throwable e){
        super(errorMsg,e);
    }

    public TransactionException(String errorMsg) {
        super(errorMsg);
    }
}
