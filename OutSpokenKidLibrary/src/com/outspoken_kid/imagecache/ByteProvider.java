package com.outspoken_kid.imagecache;

import java.io.IOException;
import java.io.OutputStream;

public interface ByteProvider {

	void writeTo(OutputStream os) throws IOException;

}