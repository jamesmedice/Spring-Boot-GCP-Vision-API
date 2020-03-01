package com.medici.app.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import com.medici.app.entity.VisionMessage;

public class ByteUtils {

	public static Image getByteString(VisionMessage message) throws IOException {
		Path path = Paths.get(message.getImageUrl());
		byte[] data = Files.readAllBytes(path);
		ByteString imgBytes = ByteString.copyFrom(data);
		return Image.newBuilder().setContent(imgBytes).build(); 
	}

}
