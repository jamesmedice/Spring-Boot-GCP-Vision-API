package com.medici.app.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
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
import com.google.protobuf.ByteString;
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
	private ResourceLoader resourceLoader;

	private ImageAnnotatorClient imageAnnotatorClient;

	@RequestMapping(value = "/faceDetection", method = RequestMethod.POST)
	public List<AnnotateImageResponse> faces(@RequestBody VisionMessage message) {
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			ByteString imgBytes = ByteUtils.getByteString(message);

			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();

			List<AnnotateImageRequest> requests = getRequests(img, feat);

			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			return responses;

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/landmarkDetection", method = RequestMethod.POST)
	public List<AnnotateImageResponse> landmarks(@RequestBody VisionMessage message) {
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			ByteString imgBytes = ByteUtils.getByteString(message);

			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();

			List<AnnotateImageRequest> requests = getRequests(img, feat);

			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			return responses;

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/docDetection", method = RequestMethod.POST)
	public List<AnnotateImageResponse> documents(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			ByteString imgBytes = ByteUtils.getByteString(message);

			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();

			List<AnnotateImageRequest> requests = getRequests(img, feat);

			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			return responses;

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/labelDetection", method = RequestMethod.POST)
	public List<AnnotateImageResponse> labels(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			ByteString imgBytes = ByteUtils.getByteString(message);

			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();

			List<AnnotateImageRequest> requests = getRequests(img, feat);

			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			return responses;

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}

	@RequestMapping(value = "/text", method = RequestMethod.POST)
	public List<AnnotateImageResponse> text(@RequestBody VisionMessage message) {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			ByteString imgBytes = ByteUtils.getByteString(message);

			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();

			List<AnnotateImageRequest> requests = getRequests(img, feat);

			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			return responses;

		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}

	}

	private List<AnnotateImageRequest> getRequests(Image img, Feature feat) {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		return requests;
	}

}
