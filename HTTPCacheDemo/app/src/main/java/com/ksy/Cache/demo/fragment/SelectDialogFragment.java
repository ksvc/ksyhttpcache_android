package com.ksy.Cache.demo.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ksy.Cache.demo.R;

/**
 * Created by xbc on 2017/8/15.
 */

public class SelectDialogFragment extends DialogFragment {

    private Button mStartPreDownload;
    private Button mStopPreDownload;
    private Button mPlayVideo;

    private OnSelectItemListener mOnSelectItemListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.SelectItemStyle);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.select_dialog, null);

        mStartPreDownload = (Button) view.findViewById(R.id.start_pre_download);
        mStopPreDownload = (Button) view.findViewById(R.id.stop_pre_download);
        mPlayVideo = (Button) view.findViewById(R.id.play_video);

        mStartPreDownload.setOnClickListener(mOnClickListener);
        mStopPreDownload.setOnClickListener(mOnClickListener);
        mPlayVideo.setOnClickListener(mOnClickListener);

        setDialogPosition(dialog);

        dialog.setContentView(view);
        return dialog;
    }

    private void setDialogPosition(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.start_pre_download:
                    if (mOnSelectItemListener != null)
                        mOnSelectItemListener.onStartPreLoad();
                    break;
                case R.id.stop_pre_download:
                    if (mOnSelectItemListener != null)
                        mOnSelectItemListener.onStopPreLoad();
                    break;
                case R.id.play_video:
                    if (mOnSelectItemListener != null)
                        mOnSelectItemListener.onPlayVideo();
                    break;
            }
            dismiss();
        }
    };

    public void setOnSelectItemListener(OnSelectItemListener listener) {
        mOnSelectItemListener = listener;
    }

    public interface OnSelectItemListener {
        void onStartPreLoad();
        void onStopPreLoad();
        void onPlayVideo();
    }
}
