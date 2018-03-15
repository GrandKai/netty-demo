package decoder;

import io.netty.handler.codec.http.HttpObjectAggregator;

public class IntegerToStringDecoder2 extends HttpObjectAggregator {

    public IntegerToStringDecoder2(int maxContentLength) {
        super(maxContentLength);
    }
}
