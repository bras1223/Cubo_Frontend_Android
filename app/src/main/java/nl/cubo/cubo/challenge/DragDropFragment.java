package nl.cubo.cubo.challenge;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import nl.cubo.cubo.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DragDropFragment.OnDropListener} interface
 * to handle interaction events.
 * Use the {@link DragDropFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DragDropFragment extends Fragment {

    ImageView element, target;

    private int number = 3;
    boolean active = false;
    boolean inDrag = false;

    OnDropListener mListener;

    public DragDropFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_drag_drop, container, false);

        element = (ImageView) view.findViewById(R.id.imageView);
        target = (ImageView) view.findViewById(R.id.target);

        element.setImageResource(this.getCurrentNumberRecource());
        target.setImageResource(R.drawable.sequence_code_target);

        element.setOnTouchListener(onTouchListener);
        target.setOnDragListener(dragListener);

        return view;
    }

    public static DragDropFragment newInstance(int number) {
        DragDropFragment fragment = new DragDropFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int getCurrentNumberRecource() {
        switch(number) {
            case 1:
                return R.drawable.sequence_code_element1;
            case 2:
                return R.drawable.sequence_code_element2;
            case 3:
                return R.drawable.sequence_code_element3;
            case 4:
                return R.drawable.sequence_code_element4;
            case 5:
                return R.drawable.sequence_code_element5;
                default:
                    return  R.drawable.sequence_code_element1;
        }
    }

    public DragDropFragment setNumber(int number) {
        this.number = number;
        return this;
    }

    public int getNumber() {
        return this.number;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void stopDrag() {
        element.setOnTouchListener(null);
    }

    public void resetState(int number) {
        this.number = number;
        if(number == 1) {
            this.active = true;
        }
        element.setVisibility(View.VISIBLE);
        element.setImageResource(this.getCurrentNumberRecource());
        target.setImageResource(R.drawable.sequence_code_target);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, myShadowBuilder, v,0);

            inDrag = true;
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DROP:
                    if(inDrag && active) {
                        element.setVisibility(View.INVISIBLE);
                        target.setImageResource(getCurrentNumberRecource());
                        mListener.onDragFinished();
                        inDrag = false;
                        active = false;
                    }
                    case DragEvent.ACTION_DRAG_ENDED:
                        if(inDrag) {
                            inDrag = false;
                            element.setVisibility(View.VISIBLE);
                        }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("hierr");
        this.setOnDropListener(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setOnDropListener(Context activity) {
        mListener = (OnDropListener) activity;
    }

    public interface OnDropListener {
        void onDragFinished();
    };

}
