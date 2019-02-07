package com.uiu.helper.KidsHelper.mvp.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetAllSlidesResponse extends BaseResponse {

	@SerializedName("slides")
	private List<SlideItem> slides;

	@SerializedName("slide")
	private SlideItem newSlide;



	public void setSlide(List<SlideItem> slide){
		this.slides = slide;
	}

	public List<SlideItem> getSlide(){

		for(SlideItem slide:slides){
			int count=1;
			for(SlideItem slide1:slides){
				if(slide.getType()==slide1.getType() && slide.getId()!=slide1.getId())
					count++;
			}
			slide.setCount(count);
		}
		Collections.sort(slides,SlideSerialComparator);
		return slides;


	}

	public SlideItem getNewSlide() {
		return newSlide;
	}

	@Override
 	public String toString(){
		return 
			"GetAllSlidesResponse{" +
			"slide = '" + slides + '\'' +
			"}";
		}

	public static Comparator<SlideItem> SlideSerialComparator
			= (slide1, slide2) -> {


		int slideType1 = slide1.getType();
		int slideType2 = slide2.getType();
		int type = ((Integer)slideType1).compareTo(slideType2);
		if(type!=0){
			return type;
		}
		int slideSerial1 = slide1.getSerial();
		int slideSerial2 = slide2.getSerial();
		//ascending order
		return ((Integer)slideSerial1).compareTo(slideSerial2);


	};
}