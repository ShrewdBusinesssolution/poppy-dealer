package com.shrewd.poppydealers.views.fragment.placeholder;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.adapters.BedsizeAdapter;
import com.shrewd.poppydealers.adapters.ImageViewPagerAdapter;
import com.shrewd.poppydealers.adapters.ProductPageAdapter;
import com.shrewd.poppydealers.adapters.SizeDAdapter;
import com.shrewd.poppydealers.adapters.ThicknesAdapter;
import com.shrewd.poppydealers.databinding.CustomListViewBinding;
import com.shrewd.poppydealers.databinding.CustomiseLayoutBinding;
import com.shrewd.poppydealers.databinding.FragmentPlaceHolderBinding;
import com.shrewd.poppydealers.databinding.ImageScreenBinding;
import com.shrewd.poppydealers.listeners.InterFaceSize;
import com.shrewd.poppydealers.listeners.InterFaceThickness;
import com.shrewd.poppydealers.listeners.InterfaceBedSize;
import com.shrewd.poppydealers.model.BedSizeModal;
import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.model.CustomModal;
import com.shrewd.poppydealers.model.Customize;
import com.shrewd.poppydealers.model.CustomizeItem;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.LoadingProgress;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.ProductPage;
import com.shrewd.poppydealers.views.activity.cart.CartViewModel;
import com.shrewd.poppydealers.views.activity.intro.ScreenItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class PlaceHolderFragment extends Fragment implements InterfaceBedSize, InterFaceSize, InterFaceThickness {
    private static final String TAG = "PlaceHolderFragment";
    private static final String ARG_SECTION_LIST = "section_list";
    public static List<CartItem> cartItems = new ArrayList<>();
    public static List<Customize> customizeList = new ArrayList<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    FragmentPlaceHolderBinding binding;
    String Code;
    String customizeTransaction = "Nothing";
    private LoadingProgress loadingProgress;
    private SessionManager sessionManager;
    private PlaceHolderViewModel productModel;
    private CartViewModel cartViewModel;
    private Animation btnAnim, slideUp, slideDown, slideLeft, slideRight, fadeIn, fadeOut;
    private Dialog dialogView;
    private ProductPageAdapter adapter;
    private int mPosition;
    private String userId, userMobile;
    private Boolean transaction = false;
    private Boolean addFlag;
    BottomSheetDialog bottomSheetDialog;

    List<BedSizeModal>[] arrayList= new ArrayList[1];
    List<BedSizeModal>[] CMOOInches= new ArrayList[1];
    private BedsizeAdapter bedAdapter;
    private SizeDAdapter SizeAdapter;
    private ThicknesAdapter Thickness;
    private List<BedSizeModal> Bedsize;
    private List<BedSizeModal> Dimension;
    private List<BedSizeModal> Thikness;
    private String Bsize= "";
    private String Ssize= "";
    private String Tsize= "";
    private String Ruppes= "";
    private String Ids,CategoryId;
    View bottomSheetView;
    private boolean selectsize;


    public PlaceHolderFragment() {
        // Required empty public constructor
    }

    public static PlaceHolderFragment newInstance(int position) {


        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPosition = getArguments().getInt("position");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlaceHolderBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {

            loadingProgress = LoadingProgress.getInstance();
            sessionManager = new SessionManager(activity);
            dialogView = new Dialog(activity, R.style.FullHeightDialog);
            dialogView.setCancelable(false);
            initAnimation(activity);

            productModel = new ViewModelProvider(this).get(PlaceHolderViewModel.class);
            cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);


            userMobile = sessionManager.getString(Constants.KEY_USER_MOBILE);
            userId = sessionManager.getString(Constants.DEALER_ID);

            binding.productPageRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


            dataset();

            cartItems = sessionManager.cartRead(getActivity());
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                cartItems.clear();
            }

            customizeList = sessionManager.customizeRead(getActivity());
            if (customizeList == null) {
                customizeList = new ArrayList<>();
                customizeList.clear();
            }

        }
    }



    private void dataset() {

        adapter = new ProductPageAdapter(ProductPage.mList.get(mPosition).getProducts(), new ProductPageAdapter.SelectedListener() {
            @Override
            public void onItemClick(int quantity, Product item) {

                cartViewModel.getCart(getActivity());

                if (item.getProduct_type().equals("1")) {
                    getBottomSheet(getActivity(), item, quantity);
                }

            }

            @Override
            public void onAddItemClick(int quantity, Product item) {

                cartViewModel.getCart(getActivity());

                if (item.getProduct_type().equals("1")) {
                    getBottomSheet(getActivity(), item, quantity);
                } else {
                    productAddtoCart(quantity, item, "toast");
                }
            }

            @Override
            public void onPlusClick(int quantity, Product item) {

                if (item.getProduct_type().equals("1")) {
                    getBottomSheet(getActivity(), item, quantity);
                } else {
                    productAddtoCart(quantity, item, "");
                }
            }

            @Override
            public void onMinusClick(int quantity, Product item) {

                if (item.getProduct_type().equals("1")) {
                    subToCart(quantity, item, "customize");
                } else {
                    subToCart(quantity, item, "normal");
                }
            }

            @Override
            public void onRemoveClick(int quantity, Product item) {


                if (item.getProduct_type().equals("1")) {
                    subToCart(quantity, item, "customize");
                } else {
                    subToCart(quantity, item, "normal");
                }
            }

            @Override
            public void onImageClick(Product item) {
                getImageScreen(item);
            }

        });

        cartViewModel.getCart(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                adapter.CartData(cartItems);

            }
        });


        binding.productPageFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Collections.sort(ProductPage.mList.get(mPosition).getProducts(), Product.LOW_TO_HIGH);
                    adapter.notifyDataSetChanged();
                } else if (position == 1) {
                    Collections.sort(ProductPage.mList.get(mPosition).getProducts(), Product.HIGH_TO_LOW);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter.notifyDataSetChanged();
        binding.productPageRecycler.setAdapter(adapter);
    }

    private void productAddtoCart(int quantity, Product item, String toast) {

        cartViewModel.addItemToCart(item, 1);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
            jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, quantity + 1);
            jsonObject.put(Constants.DEALER_ID, userId);
            jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
            jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

        } catch (Exception e) {
            e.printStackTrace();
        }

        CallRequest(jsonObject);

    }

    private void subToCart(int quantity, Product item, String type) {

        setLoading();

        JSONObject jsonObject = new JSONObject();

        cartViewModel.subItemToCart(item);

        if (type.equalsIgnoreCase("customize")) {

            customizeList = sessionManager.customizeRead(getActivity());
            if (customizeList == null) {
                customizeList = new ArrayList<>();
                customizeList.clear();
            }

            if (customizeList.size() > 0) {
                for (int i = 0; i < customizeList.size(); i++) {


                    if (Integer.parseInt(customizeList.get(i).getProduct_id()) == Integer.parseInt(item.getProduct_id())) {

                        int count = customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getGroup_count();
                        count -= 1;
                        customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).setGroup_count(count);


                        sessionManager.customizeWrite(getActivity(), customizeList);

                        try {

                            jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                            jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, quantity);
                            jsonObject.put(Constants.DEALER_ID, userId);
                            jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getGroup_id());
                            jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getGroup_count());
                            jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }

            }
        } else {
            try {

                jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, quantity);
                jsonObject.put(Constants.DEALER_ID, userId);
                jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());


        RxClient.getInstance()
                .subCart(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {


                        String message = String.valueOf(apiResponses.get("message"));
                        message = message.replaceAll("\"", "");
                        if (!message.equalsIgnoreCase("Quantity Decreased!")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getImageScreen(Product item) {

        ImageScreenBinding imageBinding = ImageScreenBinding.inflate(LayoutInflater.from(getActivity()));
        dialogView.setContentView(imageBinding.getRoot());


        if (dialogView.getWindow() != null) {
            dialogView.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialogView.getWindow().setGravity(Gravity.CENTER);

        }
        imageBinding.imagePageToolbar.commonTitle.setText(item.getProduct_name());
        imageBinding.imagePageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogView.dismiss();
            }
        });


        List<ScreenItem> mList = new ArrayList<>();

        for (int z = 0; z < item.getImageArray().length(); z++) {
            String image = null;
            try {
                image = item.getImageArray().getString(z);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mList.add(new ScreenItem(image));
        }

        ImageViewPagerAdapter introViewPagerAdapter = new

                ImageViewPagerAdapter(getActivity(), mList);
        imageBinding.imagePager.setAdapter(introViewPagerAdapter);

        imageBinding.imageIndicator.setupWithViewPager(imageBinding.imagePager);

        dialogView.show();


    }

    private void getBottomSheet(Activity activity, Product item, int quantity) {


        cartViewModel.getCart(getActivity());

        customizeList = sessionManager.customizeRead(getActivity());
        if (customizeList == null) {
            customizeList = new ArrayList<>();
            customizeList.clear();
        }

//        if (quantity == 0) {
//            quantity = quantity + 1;
//        }

        int finalQuantity = quantity;


         bottomSheetDialog = new BottomSheetDialog(activity);

         bottomSheetView = LayoutInflater.from(activity).inflate(R.layout.customise_layout, (LinearLayout) activity.findViewById(R.id.customizeSheetContainer));

        LinearLayout customizeLayout = bottomSheetView.findViewById(R.id.customizeLayout);
        LinearLayout repeatLayout = bottomSheetView.findViewById(R.id.repeatLayout);
        LinearLayout customizeSize = bottomSheetView.findViewById(R.id.customizeCustomSize);

        Button repeatItem = bottomSheetView.findViewById(R.id.repeatItem);
        Button customizeItem = bottomSheetView.findViewById(R.id.customizeItem);
        Button addToCart = bottomSheetView.findViewById(R.id.customizeAddToCart);

        TextView title = bottomSheetView.findViewById(R.id.customizeTitle);
        title.setText("Customize");
        TextView customize = bottomSheetView.findViewById(R.id.customizeCustom);
        TextView alertLength = bottomSheetView.findViewById(R.id.customizeAlertLength);
        TextView alertWidth = bottomSheetView.findViewById(R.id.customizeAlertWidth);

        TextView repeatItemModel = bottomSheetView.findViewById(R.id.repeatItemModel);
        TextView repeatItemColor = bottomSheetView.findViewById(R.id.repeatItemColor);
        TextView repeatItemSize = bottomSheetView.findViewById(R.id.repeatItemSize);
        TextView repeatItemQuantity = bottomSheetView.findViewById(R.id.repeatItemQuantity);
        RecyclerView BedsizeRecylerview = bottomSheetView.findViewById(R.id.BedSizeRecycler);
        RecyclerView SizeRecylerview = bottomSheetView.findViewById(R.id.SizeSizeRecycler);
        RecyclerView ThicknesssizeRecylerview = bottomSheetView.findViewById(R.id.ThicknessRecycler);


//        Spinner thick = bottomSheetView.findViewById(R.id.customizeThickSpinner);
//        Spinner color = bottomSheetView.findViewById(R.id.customizeColorSpinner);
//        Spinner size = bottomSheetView.findViewById(R.id.customizeSizeSpinner);


        EditText length = bottomSheetView.findViewById(R.id.customizeLength);
        EditText width = bottomSheetView.findViewById(R.id.customizeWidth);
        EditText cusQuantity = bottomSheetView.findViewById(R.id.customizeQuantity);

        Ids=item.getProduct_id();
        CategoryId=item.getCategory_id();
        RxClient.getInstance()
                .getProductCustomize(item.getCategory_id(), item.getProduct_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        String json = String.valueOf(apiResponses.get("product_data"));
                        try {
                            JSONObject jsonObject = new JSONObject(json);

                            //TODO Array CM OR Bed
//                            Bedsize[0] = new ArrayList<>();
//                            for (int i = 0; i< cust[0].get(0).getBedSizeModals().size(); i++){
//                                if (i==0){
//                                    Bedsize[0].add(new AccesSize(cust[0].get(0).getBedSizeModals().get(i).getTitleTop(),1));
//                                }else {
//                                    Bedsize[0].add(new AccesSize(cust[0].get(0).getBedSizeModals().get(i).getTitleTop(),0));
//                                }
//                            }
//                            if (Bedsize.length>0){
//                                ModalAccessories=Bedsize[0].get(0).getTitleTop();
//                            }
                            Bedsize=new ArrayList<>();
                            Dimension=new ArrayList<>();
                            Thikness=new ArrayList<>();
                            Bedsize=getBedSize(jsonObject);
                            Dimension=GetDimension(jsonObject);
                            Thikness=GetThickness(jsonObject);

                            if (Bedsize.size()>0){
                                Bedsize.get(0).setImage(1);
                                Bsize=Bedsize.get(0).getTittle();
                            }

                            if (Dimension.size()>0){
                                Dimension.get(0).setImage(1);
                                Ssize=Dimension.get(0).getTittle();
                            }
                            if (Thikness.size()>0){
                                Dimension.get(0).setImage(1);
                                Tsize=Thikness.get(0).getTittle();
                            }
                            BedsizeRecylerview.setLayoutManager(new GridLayoutManager(requireActivity(),3,GridLayoutManager.VERTICAL, false));
                            bedAdapter= new BedsizeAdapter( Bedsize,requireActivity(),PlaceHolderFragment.this::BedSelect,bottomSheetView);
                            BedsizeRecylerview.setAdapter(bedAdapter);

                            SizeRecylerview.setLayoutManager(new GridLayoutManager(requireActivity(),3,GridLayoutManager.VERTICAL, false));
                            SizeAdapter= new SizeDAdapter(Dimension,requireActivity(),PlaceHolderFragment.this::SizeSelect,bottomSheetView);
                            SizeRecylerview.setAdapter(SizeAdapter);

                            ThicknesssizeRecylerview.setLayoutManager(new GridLayoutManager(requireActivity(),3,GridLayoutManager.VERTICAL, false));
                            Thickness= new ThicknesAdapter(Thikness,requireActivity(),PlaceHolderFragment.this::ThicknessSelect,bottomSheetView);
                            ThicknesssizeRecylerview.setAdapter(Thickness);

                            getCustomreate();
//                            ArrayAdapter<String> thickAdapter = new ArrayAdapter<String>(getActivity(),
//                                    android.R.layout.simple_spinner_item, getThick(jsonObject));
//                            thickAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//                            ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(),
//                                    android.R.layout.simple_spinner_item, getColor(jsonObject));
//                            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//                            ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(getActivity(),
//                                    android.R.layout.simple_spinner_item, getSize(jsonObject));
//                            sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//                            thick.setAdapter(thickAdapter);
//                            color.setAdapter(colorAdapter);
//                            size.setAdapter(sizeAdapter);if

                                int lengthStart = jsonObject.getInt("lengthstart");
                                int lengthEnd = jsonObject.getInt("lengthend");
                                int WidthStart = jsonObject.getInt("widthstart");
                                int WidthEnd = jsonObject.getInt("widthend");




                            length.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (s != null && !String.valueOf(s).equals("")) {

                                        if (Integer.parseInt(String.valueOf(s)) >= lengthStart && Integer.parseInt(String.valueOf(s)) <= lengthEnd) {
                                            alertLength.setVisibility(View.GONE);
                                        } else {
                                            alertLength.setVisibility(View.VISIBLE);
                                            alertLength.setText("Length customization is allowed between " + lengthStart + " - " + lengthEnd + " inch");
                                        }

                                    } else {
                                        alertLength.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            width.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (s != null && !String.valueOf(s).equals("")) {

                                        if (Integer.parseInt(String.valueOf(s)) >= WidthStart && Integer.parseInt(String.valueOf(s)) <= WidthEnd) {
                                            alertWidth.setVisibility(View.GONE);
                                        } else {
                                            alertWidth.setVisibility(View.VISIBLE);
                                            alertWidth.setText("Width customization is allowed between " + WidthStart + " - " + WidthEnd + "inch");
                                        }

                                    } else {
                                        alertWidth.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

//                            color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                                    try {
//                                        Code = getSelectCode(color.getSelectedItemPosition(), jsonObject);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customizeList = sessionManager.customizeRead(getActivity());
                if (customizeList == null) {
                    customizeList = new ArrayList<>();
                    customizeList.clear();
                }

                if (transaction) {
                    customizeTransaction = "Nothing";
                    customize.setText(R.string.normalsize);
                    SizeRecylerview.setVisibility(View.VISIBLE);
                    customizeSize.setVisibility(View.GONE);
                } else {
                    customizeTransaction = "Something";
                    SizeRecylerview.setVisibility(View.GONE);
                    customizeSize.setVisibility(View.VISIBLE);
                    customize.setText(R.string.abnormalsize);
                }
                transaction = !transaction;


            }
        });

        customizeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatLayout.setVisibility(View.GONE);
                repeatLayout.startAnimation(slideDown);

                customizeLayout.setVisibility(View.VISIBLE);
                customizeLayout.startAnimation(slideUp);
            }
        });


        if (customizeList.size() > 0) {
            for (int i = 0; i < customizeList.size(); i++) {

                if (Integer.parseInt(customizeList.get(i).getProduct_id()) == Integer.parseInt(item.getProduct_id())) {

//                    Toast.makeText(getActivity(), customizeList.get(i).getProduct_id()+", "+item.getProduct_id(), Toast.LENGTH_SHORT).show();


                    repeatItemModel.setText(customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getInches());
                    repeatItemColor.setText(", " + customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getColor());
                    repeatItemSize.setText(", " + customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getSize());
                    repeatItemQuantity.setText("x" + customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getGroup_count());


                    customizeLayout.setVisibility(View.GONE);
                    repeatLayout.setVisibility(View.VISIBLE);

                    break;
                }

            }

        }

        repeatItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLoading();
                // TODO DUMMy BAVA
                customizeList = sessionManager.customizeRead(getActivity());
                if (customizeList == null) {
                    customizeList = new ArrayList<>();
                    customizeList.clear();
                }

                cartViewModel.addItemToCart(item, 1);

                JSONObject jsonObject = new JSONObject();

                if (customizeList.size() > 0) {
                    for (int i = 0; i < customizeList.size(); i++) {
                        if (Integer.parseInt(customizeList.get(i).getProduct_id()) == Integer.parseInt(item.getProduct_id())) {

                            int lastCount = customizeList.get(i).getList().size() - 1;


                            int count = customizeList.get(i).getList().get(lastCount).getGroup_count();
                            count += 1;
                            customizeList.get(i).getList().get(lastCount).setGroup_count(count);

                            sessionManager.customizeWrite(getActivity(), customizeList);

                            repeatItemModel.setText(customizeList.get(i).getList().get(lastCount).getInches());
                            repeatItemColor.setText(", " + customizeList.get(i).getList().get(lastCount).getColor());
                            repeatItemQuantity.setText("x" + customizeList.get(i).getList().get(lastCount).getGroup_count());


                            customizeLayout.setVisibility(View.GONE);
                            repeatLayout.setVisibility(View.VISIBLE);


                            try {
                                jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, finalQuantity + 1);
                                jsonObject.put(Constants.DEALER_ID, userId);
                                jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
                                jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, customizeList.get(i).getList().get(lastCount).getGroup_id());
                                jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, customizeList.get(i).getList().get(lastCount).getGroup_count());
                                jsonObject.put(Constants.KEY_PRODUCT_INCHES, customizeList.get(i).getList().get(lastCount).getInches());
                                jsonObject.put(Constants.KEY_PRODUCT_SIZE, customizeList.get(i).getList().get(lastCount).getSize());
                                jsonObject.put(Constants.KEY_PRODUCT_COLOR, customizeList.get(i).getList().get(lastCount).getColor());
                                jsonObject.put(Constants.KEY_PRODUCT_COLOR_CODE, customizeList.get(i).getList().get(lastCount).getColor_code());
                                jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CallRequest(jsonObject);

                            break;
                        }

                    }

                }

            }
        });

        for (Customize cus : customizeList) {
            for (CustomizeItem c : cus.getList()) {

                Log.d(TAG, "MessageData:::main--pro_id::" + cus.getProduct_id() + ", group_id::" + c.getGroup_id() + ", group_count::" + c.getGroup_count());

            }

        }

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();

                if (!cusQuantity.getText().toString().equals("")) {

                    int qua = Integer.parseInt(cusQuantity.getText().toString());
                    //TODO Today
                    String Inches = Tsize;
                    String Color = Ruppes;
                    Code=Bsize;
                    String Size = null;


                    if (customizeTransaction.equalsIgnoreCase("Nothing")) {
                        Size = Ssize;
                    } else {
                        Size = length.getText().toString() + "X" + width.getText().toString();
                    }

                    String currentitem = Inches + "," + Color + "," + Code + "," + Size;


                    cartViewModel.addItemToCart(item, qua);

                    if (customizeList.size() > 0) {
                        for (int i = 0; i < customizeList.size(); i++) {

                            if (Integer.parseInt(customizeList.get(i).getProduct_id()) == Integer.parseInt(item.getProduct_id())) {

                                addFlag = true;

                                int lastCount = customizeList.get(i).getList().size() - 1;

                                String lastsameitem = customizeList.get(i).getList().get(lastCount).getInches() + "," + customizeList.get(i).getList().get(lastCount).getColor() + "," + customizeList.get(i).getList().get(lastCount).getColor_code() + "," + customizeList.get(i).getList().get(lastCount).getSize();

                                if (currentitem.equalsIgnoreCase(lastsameitem)) {

                                    int count = customizeList.get(i).getList().get(lastCount).getGroup_count();
                                    count += qua;
                                    customizeList.get(i).getList().get(lastCount).setGroup_count(count);

                                    sessionManager.customizeWrite(getActivity(), customizeList);
                                    try {

                                        jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                                        jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, finalQuantity + qua);
                                        jsonObject.put(Constants.DEALER_ID, userId);
                                        jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
                                        jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, customizeList.get(i).getList().get(lastCount).getGroup_id());
                                        jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, customizeList.get(i).getList().get(lastCount).getGroup_count());
                                        jsonObject.put(Constants.KEY_PRODUCT_INCHES, customizeList.get(i).getList().get(lastCount).getInches());
                                        jsonObject.put(Constants.KEY_PRODUCT_SIZE, customizeList.get(i).getList().get(lastCount).getSize());
                                        jsonObject.put(Constants.KEY_PRODUCT_COLOR, customizeList.get(i).getList().get(lastCount).getColor());
                                        jsonObject.put(Constants.KEY_PRODUCT_COLOR_CODE, customizeList.get(i).getList().get(lastCount).getColor_code());

                                        jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    CallRequest(jsonObject);


                                } else {

                                    int groupId = customizeList.get(i).getList().get(lastCount).getGroup_id() + 1;


                                    List<CustomizeItem> list = customizeList.get(i).getList();
                                    list.add(new CustomizeItem(groupId, qua, Inches, Color, Code, Size));
                                    customizeList.add(new Customize(item.getProduct_id(), list));


                                    sessionManager.customizeWrite(getActivity(), customizeList);

                                    try {

                                        jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                                        jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, finalQuantity + qua);
                                        jsonObject.put(Constants.DEALER_ID, userId);
                                        jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
                                        jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, groupId);
                                        jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, qua);
                                        jsonObject.put(Constants.KEY_PRODUCT_INCHES, Inches);
                                        jsonObject.put(Constants.KEY_PRODUCT_SIZE, Size);
                                        jsonObject.put(Constants.KEY_PRODUCT_COLOR, Color);
                                        jsonObject.put(Constants.KEY_PRODUCT_COLOR_CODE, Code);

                                        jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    CallRequest(jsonObject);

                                }

                                break;
                            } else {

                                addFlag = false;
                            }

                        }

                        if (addFlag == false) {

                            try {

                                jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, finalQuantity + qua);
                                jsonObject.put(Constants.DEALER_ID, userId);
                                jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
                                jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, "1");
                                jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, qua);
                                jsonObject.put(Constants.KEY_PRODUCT_INCHES, Inches);
                                jsonObject.put(Constants.KEY_PRODUCT_SIZE, Size);
                                jsonObject.put(Constants.KEY_PRODUCT_COLOR, Color);
                                jsonObject.put(Constants.KEY_PRODUCT_COLOR_CODE, Code);

                                jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            CallRequest(jsonObject);


                        }

                    } else {
                        List<CustomizeItem> customizeItems = new ArrayList<>();
                        customizeItems.add(new CustomizeItem(1, qua, Inches, Color, Code, Size));

                        customizeList.add(new Customize(item.getProduct_id(), customizeItems));

                        sessionManager.customizeWrite(getActivity(), customizeList);

                        try {

                            jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                            jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, finalQuantity + qua);
                            jsonObject.put(Constants.DEALER_ID, userId);
                            jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
                            jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, "1");
                            jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, qua);
                            jsonObject.put(Constants.KEY_PRODUCT_INCHES, Inches);
                            jsonObject.put(Constants.KEY_PRODUCT_SIZE, Size);
                            jsonObject.put(Constants.KEY_PRODUCT_COLOR, Color);
                            jsonObject.put(Constants.KEY_PRODUCT_COLOR_CODE, Code);

                            jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        CallRequest(jsonObject);

                    }


//                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "please type your quantity", Toast.LENGTH_SHORT).show();
                }


            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void CallRequest(JSONObject jsonObject) {
        setLoading();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .addCart(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        String message = String.valueOf(apiResponses.get("message"));
                        message = message.replaceAll("\"", "");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        customizeTransaction = "Nothing";
                        cartViewModel.getCart(getActivity());
                        bottomSheetDialog.dismiss();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void setLoading() {

    }

    private String getSelectCode(int position, JSONObject jsonObject) throws JSONException {
        String code = "";
        JSONArray object = jsonObject.getJSONArray("color");
        for (int j = 0; j < object.length(); j++) {
            JSONObject gobject = object.getJSONObject(position);
            code = gobject.getString("color_code");
        }

        return code;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (binding.productPageRecycler != null) {
            dataset();
        }
    }

    private void initAnimation(Activity activity) {
        slideLeft = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left);
        slideRight = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right);
        fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
        slideUp = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top);
        slideDown = AnimationUtils.loadAnimation(activity, R.anim.slide_in_down);
    }


    private List<BedSizeModal> getBedSize(JSONObject jsonObject) throws JSONException {
        List<BedSizeModal> spinner = new ArrayList<>();

            JSONArray object = jsonObject.getJSONArray("bed_type");
        for (int j = 0; j < object.length(); j++) {
            JSONObject gobject = object.getJSONObject(j);
            if (j==0){
                spinner.add(new BedSizeModal(gobject.getString("type"),1));
            }else {
                spinner.add(new BedSizeModal(gobject.getString("type"),0));
            }

        }

        return spinner;
    }

    private List<BedSizeModal> GetDimension(JSONObject jsonObject) throws JSONException {
        List<BedSizeModal> spinner = new ArrayList<>();

        JSONArray object = jsonObject.getJSONArray("dimension");
        for (int j = 0; j < object.length(); j++) {
            JSONObject gobject = object.getJSONObject(j);
            if (j==0){
                spinner.add(new BedSizeModal(gobject.getString("dimension"),1));
            }else {
                spinner.add(new BedSizeModal(gobject.getString("dimension"),0));
            }

        }

        return spinner;
    }


    private List<BedSizeModal> GetThickness(JSONObject jsonObject) throws JSONException {
        List<BedSizeModal> spinner = new ArrayList<>();

        JSONArray object = jsonObject.getJSONArray("thickness");
        for (int j = 0; j < object.length(); j++) {
            JSONObject gobject = object.getJSONObject(j);
            if (j==0){
                spinner.add(new BedSizeModal(gobject.getString("thickness"),1));
            }else {
                spinner.add(new BedSizeModal(gobject.getString("thickness"),0));
            }

        }

        return spinner;
    }

    private List<String> getColor(JSONObject jsonObject) throws JSONException {

        List<String> spinner = new ArrayList<>();

        JSONArray object = jsonObject.getJSONArray("thickness");
        for (int j = 0; j < object.length(); j++) {
            JSONObject gobject = object.getJSONObject(j);
            spinner.add(gobject.getString("thickness"));
        }

        return spinner;
    }

    private List<String> getSize(JSONObject jsonObject) throws JSONException {

        List<String> spinner = new ArrayList<>();

        JSONArray object = jsonObject.getJSONArray("dimension");
        for (int j = 0; j < object.length(); j++) {
            JSONObject gobject = object.getJSONObject(j);
            spinner.add(gobject.getString("dimension"));
        }

        return spinner;
    }


    @Override
    public void BedSelect(BedSizeModal bedsize, int pos, View binding) {
        for (int t=0;t<Bedsize.size();t++){
            Bedsize.get(t).setImage(0);
        }
        Bedsize.get(pos).setImage(1);
        Bsize=bedsize.getTittle();
        bedAdapter.notifyDataSetChanged();
        Ssize="";
        Tsize="";
        Dimension.clear();
        Thikness.clear();
        selectsize=true;
        TextView textView=(TextView) bottomSheetView.findViewById(R.id.RateCus);
        textView.setText("0.00");
        ApiCustomThin(Integer.parseInt(Ids),CategoryId,Bsize,"",binding);
    }

    @Override
    public void SizeSelect(BedSizeModal bedsize, int pos, View binding) {
        for (int t=0;t<Dimension.size();t++){
            Dimension.get(t).setImage(0);
        }
        Dimension.get(pos).setImage(1);
        Ssize=bedsize.getTittle();
        Tsize="";
        Thikness.clear();
        SizeAdapter.notifyDataSetChanged();
        selectsize=false;
        TextView textView=(TextView) bottomSheetView.findViewById(R.id.RateCus);
        textView.setText("0.00");
        ApiCustomThin(Integer.parseInt(Ids),CategoryId,Bsize,Ssize,binding);
    }

    @Override
    public void ThicknessSelect(BedSizeModal bedsize, int pos, View binding) {
        for (int t=0;t<Thikness.size();t++){
            Thikness.get(t).setImage(0);
        }
     try {
        Thikness.get(pos).setImage(1);

     } catch (Exception e) {
         e.printStackTrace();
     }
        Tsize=bedsize.getTittle();
        Thickness.notifyDataSetChanged();
        TextView textView=(TextView) bottomSheetView.findViewById(R.id.RateCus);
        textView.setText("0.00");
        getCustomreate();
    }

    private void getCustomreate() {
        productModel.InitGetCUSRate(Integer.parseInt(Ids),CategoryId,Bsize,Ssize,Tsize).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                TextView textView=(TextView) bottomSheetView.findViewById(R.id.RateCus);
                if (s!=null){

                    textView.setText(s);
                    Ruppes=s;

                }else {
                    textView.setText("0.00");
                }
            }
        });
    }



    private void ApiCustomThin(int ids, String catoegory_id, String modalAccessories, String Dimen, View bind) {

        productModel.GetCustomModel(ids,catoegory_id,modalAccessories,Dimen).observe(getViewLifecycleOwner(), new Observer<List<CustomModal>>() {
            @Override
            public void onChanged(List<CustomModal> customizeModals) {
                if (customizeModals!=null){
//                    bind.customiseSizeShimmer.stopShimmer();
//                    bind.customiseSizeShimmer.hideShimmer();
//                    bind.customiseSizeShimmer.setVisibility(View.GONE);
//
//                    bind.size.setVisibility(View.VISIBLE);
//                    bind.inchestitleshimer.setVisibility(View.GONE);
//                    bind.inchestitleshimer.stopShimmer();
//                    bind.inchestitleshimer.hideShimmer();
//                    bind.recyclerThickness.setVisibility(View.VISIBLE);






//                    RecyclerView Bedrecycler= bind.findViewById(R.id.BedSizeRecycler);


//                    Bedrecycler.setLayoutManager(new GridLayoutManager(requireActivity(),3,GridLayoutManager.VERTICAL, false));
//                    bedAdapter= new BedsizeAdapter( Bedsize,requireActivity(),PlaceHolderFragment.this::BedSelect,bind);
//                    Bedrecycler.setAdapter(bedAdapter);
                    if (selectsize) {
                        Dimension= customizeModals.get(0).getAccesSizes();
                        getViewDimension(Dimension,bind);

                    }

                    Thikness= customizeModals.get(0).getIncheslist();
                    if (customizeModals.get(0).getIncheslist()!=null && !customizeModals.get(0).getIncheslist().isEmpty()){
                        getViewThickness(Thikness,bind);
                    }


                }
            }
        });

    }

    private void getViewThickness(List<BedSizeModal> thikness, View bind) {
        RecyclerView Thinrecycler= bottomSheetView.findViewById(R.id.ThicknessRecycler);
        Thinrecycler.setLayoutManager(new GridLayoutManager(requireActivity(),3,GridLayoutManager.VERTICAL, false));
        Thickness= new ThicknesAdapter( thikness,requireActivity(),PlaceHolderFragment.this::ThicknessSelect,bind);
        Thinrecycler.setAdapter(Thickness);
        Thickness.notifyDataSetChanged();
    }

    private void getViewDimension(List<BedSizeModal> dimension, View bind) {
        RecyclerView Sizerecycler= bottomSheetView.findViewById(R.id.SizeSizeRecycler);
        Sizerecycler.setLayoutManager(new GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false));
        SizeAdapter = new SizeDAdapter(dimension, requireActivity(), PlaceHolderFragment.this::SizeSelect, bind);
        Sizerecycler.setAdapter(SizeAdapter);
        SizeAdapter.notifyDataSetChanged();

    }
}




//        Typeface light = ResourcesCompat.getFont(activity, R.font.exo_2_light);
//        Typeface medium = ResourcesCompat.getFont(activity, R.font.exo_2_medium);
//        Typeface semi = ResourcesCompat.getFont(activity, R.font.exo_2_semibold);
//        Typeface bold = ResourcesCompat.getFont(activity, R.font.exo_2_bold);
//
//        title.setTypeface(bold);
//        Size.setTypeface(semi);
//        Accessory.setTypeface(semi);
//        Color.setTypeface(semi);


//        FrameLayout back = bottomSheetView.findViewById(R.id.customizeBack);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });