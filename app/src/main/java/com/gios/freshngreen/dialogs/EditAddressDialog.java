package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.profile.AreaList;
import com.gios.freshngreen.responseModel.profile.PinList;
import com.gios.freshngreen.responseModel.profile.UserDetail;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EditAddressDialog extends BottomSheetDialogFragment implements AreaDialog.SelectAreaInterface,
        PincodeDialog.SelectPincodeInterface{

    private EditAddressDialog.EditAddressDialogInterface mInterface;
    private UserDetail userDetail;
    private List<AreaList> areaList = new ArrayList<>();
    private List<PinList> pinList = new ArrayList<>();

    AppCompatEditText address1;
    AppCompatEditText address2 ;
    AppCompatEditText area ;
    AppCompatEditText city ;
    AppCompatEditText pinCode ;
    AppCompatEditText landmark ;
    Button editBtn;
    String pinId ="",areaId = "";

    public EditAddressDialog(EditAddressDialog.EditAddressDialogInterface mInterface,
                             UserDetail userDetail, List<AreaList> areaList, List<PinList> pinList) {
        this.mInterface = mInterface;
        this.userDetail = userDetail;
        this.areaList = areaList;
        this.pinList = pinList;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.edit_address_dialog, null);
        dialog.setContentView(contentView);

         address1 = contentView.findViewById(R.id.address1);
         address2 = contentView.findViewById(R.id.address2);
         area = contentView.findViewById(R.id.area);
         city = contentView.findViewById(R.id.city);
         pinCode = contentView.findViewById(R.id.pinCode);
         landmark = contentView.findViewById(R.id.landmark);
         editBtn = (Button)contentView.findViewById(R.id.editBtn);

        address1.setText(userDetail.getAddress());
        area.setText(userDetail.getArea());
        city.setText(userDetail.getCity());
        pinCode.setText(userDetail.getPin());
        landmark.setText(userDetail.getLandmark());

        if(userDetail.getArea() != null) {
            for (AreaList mAreaList : areaList) {
                if (userDetail.getArea().equals(mAreaList.getAreaName())) {
                    areaId = mAreaList.getId();
                }
            }
        }
        if(userDetail.getPin() != null) {
            for (PinList mPinList : pinList) {
                if (userDetail.getPin().equals(mPinList.getPin())) {
                    pinId = mPinList.getId();
                }
            }
        }

        area.setOnClickListener(v ->
        {
            AreaDialog mAreaDialog = new AreaDialog(this, areaList);
            mAreaDialog.show(getParentFragmentManager(), TAG);
        });

        pinCode.setOnClickListener(v ->
        {
            PincodeDialog mPincodeDialog = new PincodeDialog(this, pinList);
            mPincodeDialog.show(getParentFragmentManager(), TAG);
        });

        editBtn.setOnClickListener(v -> {
            if(address1.getText().toString().trim().isEmpty()){
                address1.setError("Please enter your Address line 1");
            }
            else if(area.getText().toString().trim().isEmpty()){
                area.setError("Please enter your Area");
            }
            else if(city.getText().toString().trim().isEmpty()){
                area.setError("Please enter your City");
            }
            else if(pinCode.getText().toString().trim().isEmpty()){
                pinCode.setError("Please enter your Pin Code");
            }
           else {
                mInterface.onAddressClick(address1.getText().toString().trim(), address2.getText().toString().trim()
                        , areaId, city.getText().toString().trim(), pinId
                        , landmark.getText().toString().trim());
                dismiss();
            }
        });
    }

    @Override
    public void selectAreaCallback(AreaList list) {
        area.setText(list.getAreaName());
        areaId = list.getId();

    }

    @Override
    public void selectPincodeCallback(PinList list) {
        pinCode.setText(list.getPin());
        pinId = list.getId();

    }

    public interface EditAddressDialogInterface {
        void onAddressClick(String address1, String address2,
                            String area,String city,String pinCode,String landmark);
        void onCancelClick();
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        mInterface.onCancelClick();
    }

}