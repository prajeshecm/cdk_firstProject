package com.myorg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awscdk.services.lambda.IFunction;

public interface HitCounterProps {

    static final Logger logger =   LoggerFactory.getLogger(HitCounterProps.class);

    IFunction getUrlCount();

    public static Builder builder() {
        logger.info("HitCounterProps builder : ");
        return new Builder();
    }

    public static class Builder{

        private IFunction getUrlCount;

        public Builder getUrlCount(final IFunction function){
            logger.info("HitCounterProps getUrlCount : ");
            this.getUrlCount = function ;
            return this;
        }

        public HitCounterProps build(){
            logger.info("HitCounterProps build : ");
            if(this.getUrlCount == null)
                throw new NullPointerException( " url count is required ");

            return  new HitCounterProps(){
                @Override
                public IFunction getUrlCount(){
                    return getUrlCount;
                }
            };
        }
    }
}
