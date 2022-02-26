package com.shrewd.poppydealers.adapters;

//public class TestingAdapter extends ListAdapter<Dummy, TestingAdapter.TestViewHolder> {
//
//    public TestingAdapter() {
//        super(Dummy.itemCallback);
//    }
//
//    @NonNull
//    @Override
//    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        ProductItemBinding productItemBinding = ProductItemBinding.inflate(layoutInflater, parent, false);
//        return new TestViewHolder(productItemBinding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
//        Dummy product = getItem(position);
//
//        holder.productItemBinding.setProduct(product);
//        holder.productItemBinding.executePendingBindings();
//    }
//
//    class TestViewHolder extends RecyclerView.ViewHolder {
//
//        ProductItemBinding productItemBinding;
//
//        public TestViewHolder(@NonNull ProductItemBinding itemView) {
//            super(itemView.getRoot());
//            this.productItemBinding = itemView;
//        }
//
//    }
//    public interface TestInterface{
//        void addItem(Dummy dummy);
//
//    }
//}
