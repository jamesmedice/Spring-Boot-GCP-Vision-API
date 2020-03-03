package com.medici.app.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.medici.app.entity.VisionMessage;
import com.medici.app.utils.ByteUtils;

/**
 * 
 * @author a73s
 *
 */
@RestController
public class VisionController {

	protected Logger logger = Logger.getLogger(VisionController.class.getName());

	@Autowired
	ResourceLoader resourceLoader;

	@RequestMapping(value = "/faceDetection", method = RequestMethod.POST)
	public String faces(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getImageAnnotatorSettings())) {
			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);

			Map<FieldDescriptor, Object> out = new HashMap<>();
			responses.stream().forEach(r -> r.getFaceAnnotationsList().stream().forEach(entity -> {
				out.putAll(entity.getAllFields());
			}));

			return responses.toString();

		} catch (IOException e) {
			return org.apache.commons.lang3.StringUtils.EMPTY;
		}
	}

	@RequestMapping(value = "/labelDetection", method = RequestMethod.POST)
	public String labels(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getImageAnnotatorSettings())) {

			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);

			return responses.toString();

		} catch (IOException e) {
			return org.apache.commons.lang3.StringUtils.EMPTY;
		}
	}

	@RequestMapping(value = "/text", method = RequestMethod.POST)
	public String text(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getImageAnnotatorSettings())) {

			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);
			return responses.toString();

		} catch (IOException e) {
			return org.apache.commons.lang3.StringUtils.EMPTY;
		}

	}

	private ImageAnnotatorSettings getImageAnnotatorSettings() throws FileNotFoundException, IOException {
		Resource resource = resourceLoader.getResource("classpath:key.json");
		Credentials myCredentials = ServiceAccountCredentials.fromStream(resource.getInputStream());
		ImageAnnotatorSettings imageAnnotatorSettings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(myCredentials)).build();
		return imageAnnotatorSettings;
	}

	private List<AnnotateImageResponse> getResponses(ImageAnnotatorClient vision, Feature feat, Image img) {

		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(Arrays.asList(request));
		List<AnnotateImageResponse> responses = response.getResponsesList();
		return responses;
	}

}
