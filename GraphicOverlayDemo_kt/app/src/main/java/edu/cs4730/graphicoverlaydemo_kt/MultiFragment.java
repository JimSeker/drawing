package edu.cs4730.graphicoverlaydemo_kt;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MultiFragment extends Fragment {

    TextView tv5;

    public MultiFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_multi, container, false);
        tv5 = (TextView) myView.findViewById(R.id.textView5);
        tv5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                // Retrieve the new x and y touch positions

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        tv5.setText(" Can't draw here!");
                        break;
                    case MotionEvent.ACTION_UP:
                        tv5.setText("Can't draw here!");
                        v.performClick();
                        break;
                }
                return false;
            }
        });
        return myView;
    }

}
