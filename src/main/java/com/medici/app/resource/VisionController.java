package com.medici.app.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
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

	@RequestMapping(value = "/faceDetection", method = RequestMethod.POST)
	public Collection<?> faces(@RequestBody VisionMessage message) {
		try {

			ImageAnnotatorClient vision = ImageAnnotatorClient.create();
			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();

			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);

			Map<FieldDescriptor, Object> payload = convertResponseToPayload(responses);
			return payload.values();

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/landmarkDetection", method = RequestMethod.POST)
	public Collection<?> landmarks(@RequestBody VisionMessage message) {
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();

			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);
			Map<FieldDescriptor, Object> payload = convertResponseToPayload(responses);

			return payload.values();

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/docDetection", method = RequestMethod.POST)
	public Collection<?> documents(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();

			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);
			Map<FieldDescriptor, Object> payload = convertResponseToPayload(responses);

			return payload.values();

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/labelDetection", method = RequestMethod.POST)
	public Collection<?> labels(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();

			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);
			Map<FieldDescriptor, Object> payload = convertResponseToPayload(responses);

			return payload.values();

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/text", method = RequestMethod.POST)
	public Collection<?> text(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			Image img = ByteUtils.getByteString(message);
			Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();

			List<AnnotateImageResponse> responses = getResponses(vision, feat, img);
			Map<FieldDescriptor, Object> payload = convertResponseToPayload(responses);

			return payload.values();

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}

	}

	private List<AnnotateImageResponse> getResponses(ImageAnnotatorClient vision, Feature feat, Image img) {

		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(Arrays.asList(request));
		List<AnnotateImageResponse> responses = response.getResponsesList();
		return responses;
	}

	private Map<FieldDescriptor, Object> convertResponseToPayload(List<AnnotateImageResponse> responses) {
		Map<FieldDescriptor, Object> out = new HashMap<>();
		responses.stream().forEach(r -> r.getWebDetection().getWebEntitiesList().stream().forEach(entity -> {
			out.putAll(entity.getAllFields());
		}));
		return out;
	}
}
