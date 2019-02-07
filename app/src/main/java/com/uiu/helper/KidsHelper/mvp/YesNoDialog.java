package com.uiu.helper.KidsHelper.mvp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uiu.helper.R;


public class YesNoDialog extends Dialog implements OnTouchListener
{
	private static final String TAG_YES_BUTTON = "yesbtn";
	private static final String TAG_NO_BUTTON = "nobtn";
	
	public static final int NOTIFICATION_DIALOG = 1;
	public static final int ALERT_DIALOG = 2;

	protected Button mButtonYes;
	protected Button mButtonNo;
	protected TextView mTextNameToDelete;
	protected TextView mTextTitle;
	protected ImageView mImageBackground;
	protected ImageView mImageShadow;
	protected Context mContext;
	protected int mIAlertOrNotification;
	
	public YesNoDialog(Context context, String title, String body, int iAlertOrNotification)
	{
		super(context, android.R.style.Theme_Translucent_NoTitleBar);

		mContext = context;
		
		mIAlertOrNotification = iAlertOrNotification;

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.yes_no_dialog);

		initComponents();

		mTextNameToDelete.setText(body);
		mTextTitle.setText(title);

		Typeface tp = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaNeueLTCom-Lt.ttf");
		mTextTitle.setTypeface(tp);
		
		mButtonYes.setTypeface(tp);		
		mButtonNo.setTypeface(tp);
		
		mButtonYes.setTextSize(24);
		mButtonNo.setTextSize(24);

		tp = Typeface.createFromAsset(mContext.getAssets(), "fonts/DroidSerif.ttf");
		mTextNameToDelete.setTypeface(tp);					
		
	}
	
	public void setButtonsText(String strLeftButtonText,String strRightButtonText)
	{
		mButtonYes.setText(strLeftButtonText);		
		mButtonNo.setText(strRightButtonText);
	}

	@SuppressWarnings("deprecation")
	private void initComponents()
	{
		mButtonYes = (Button) findViewById(R.id.yes_no_dialog_button_yes);
		mButtonYes.setTag(TAG_YES_BUTTON);
		
		mButtonNo = (Button) findViewById(R.id.yes_no_dialog_button_no);
		mButtonNo.setTag(TAG_NO_BUTTON);
		
		mTextNameToDelete = (TextView) findViewById(R.id.yes_no_dialog_text_delete);
		mTextTitle = (TextView) findViewById(R.id.yes_no_dialog_text_title);
		
		if(mIAlertOrNotification == NOTIFICATION_DIALOG)
		{
			Drawable background = getContext().getResources().getDrawable(R.drawable.dialog_header_blue);
			mTextTitle.setBackgroundDrawable(background);
		}
		else if(mIAlertOrNotification == ALERT_DIALOG)
		{
			Drawable background = getContext().getResources().getDrawable(R.drawable.dialog_header_red);
			mTextTitle.setBackgroundDrawable(background);
		}
		
		
		setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog)
			{
				/*
				int height = getDialogContentHeight();
				int width = getDialogContentWidth();

				Drawable drawable = getContext().getResources().getDrawable(R.drawable.main_background);
				mDeleteDialogBitmap = ((BitmapDrawable) drawable).getBitmap();

				mDeleteDialogBitmap = Bitmap.createBitmap(mDeleteDialogBitmap, 0, 0, width, height);

				setBackground(mDeleteDialogBitmap);

				updateParamRules();*/

			}
		});
		
		mButtonYes.setOnTouchListener(this);
		mButtonNo.setOnTouchListener(this);
	}

	public void setPositiveButtonListener(final View.OnClickListener listener)
	{
		mButtonYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				mButtonYes.setClickable(false);
				listener.onClick(mButtonYes);
			}
		});
	}

	public void setNegativeButtonListener(final View.OnClickListener listener)
	{
		mButtonNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{				
				listener.onClick(mButtonNo);
			}
		});
	}	

	public void setDialogContentMaxHeight(){}
	
	public void setBackground(Bitmap bitmap)
	{
	if (bitmap != null)
		{
			mImageBackground.setImageBitmap(bitmap);
		}
	}	
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		final int action = event.getAction();
		
		switch(action){
			
			case MotionEvent.ACTION_DOWN:
			{
				v.setBackgroundResource(R.drawable.small_action_button_flat_feedback);
			}
			break;
				
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
			{				
				if(v.getTag().equals(TAG_YES_BUTTON))
				{
					v.setBackgroundResource(R.drawable.small_action_button_flat_green);					
				}
				else if(v.getTag().equals(TAG_NO_BUTTON))
				{
					v.setBackgroundResource(R.drawable.small_action_button_flat_red);
				}
			}
			
		break;
		}
		
		return false;
	}
}
