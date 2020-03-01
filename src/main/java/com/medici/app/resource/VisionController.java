package com.medici.app.resource;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.medici.app.entity.VisionMessage;

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

	@Autowired
	private CloudVisionTemplate cloudVisionTemplate;

	//

	@RequestMapping(value = "/faceDetection", method = RequestMethod.POST)
	public List<FaceAnnotation> faces(@RequestBody VisionMessage message) {
		try {
			AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(this.resourceLoader.getResource(message.getImageUrl()), Type.FACE_DETECTION);
			return response.getFaceAnnotationsList();
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/landmarkDetection", method = RequestMethod.POST)
	public List<EntityAnnotation> landmarks(@RequestBody VisionMessage message) {
		try {
			AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(this.resourceLoader.getResource(message.getImageUrl()), Type.LANDMARK_DETECTION);
			return response.getLandmarkAnnotationsList();
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/docDetection", method = RequestMethod.POST)
	public List<EntityAnnotation> documents(@RequestBody VisionMessage message) {
		try {
			AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(this.resourceLoader.getResource(message.getImageUrl()), Type.DOCUMENT_TEXT_DETECTION);
			return response.getTextAnnotationsList();
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/labelDetection", method = RequestMethod.POST)
	public List<EntityAnnotation> labels(@RequestBody VisionMessage message) {
		try {
			AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(this.resourceLoader.getResource(message.getImageUrl()), Type.LABEL_DETECTION);
			return response.getLabelAnnotationsList();
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/text", method = RequestMethod.POST)
	public VisionMessage translate(@RequestBody VisionMessage message) {
		try {
			String text = this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(message.getImageUrl()));
			message.setText(text);
			return message;
		} catch (Exception e) {
			return null;
		}
	}

}
