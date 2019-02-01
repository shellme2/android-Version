package com.eebbk.bfc.sdk.version;

@Deprecated
public class VMException extends Exception{
	  /**
		 * 可序列化
		 */
		private static final long serialVersionUID = 1L;
		
		private final int mStatus;

		public VMException(int finalStatus, String message) {
	        super(message);
	        mStatus = finalStatus;
	    }

		public  VMException(int finalStatus, String message, Throwable throwable) {
	        super(message, throwable);
	        mStatus = finalStatus;
	    }
	    
	    public int getStatus(){
	    	return mStatus;
	    }
}
