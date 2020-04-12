package com.alexGens.credit_calculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExtraPaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExtraPaymentsFragment extends Fragment {
    private ImageView destroyFrag;
    private AddExtraPaymentsFragment fragment = this;
    private View v;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddExtraPaymentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment extraPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddExtraPaymentsFragment newInstance(String param1, String param2) {
        AddExtraPaymentsFragment fragment = new AddExtraPaymentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void destroyFrag() {
        destroyFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_extra_payment, container, false);
        destroyFrag = (ImageView) v.findViewById(R.id.fragment_destroy);
        destroyFrag();
        return v;

    }
}
