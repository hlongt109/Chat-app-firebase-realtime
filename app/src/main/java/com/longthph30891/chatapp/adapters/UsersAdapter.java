package com.longthph30891.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longthph30891.chatapp.databinding.ItemContainerUserBinding;
import com.longthph30891.chatapp.listener.UserListener;
import com.longthph30891.chatapp.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
    private final List<User> userList;
    private final UserListener userListener;


    public UsersAdapter(List<User> userList,UserListener userListener) {
        this.userList = userList;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding binding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
       ItemContainerUserBinding binding;
       UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
           super(itemContainerUserBinding.getRoot());
           binding = itemContainerUserBinding;
       }
       void setUserData(User user){
           binding.tvName.setText(user.getName());
           binding.tvEmail.setText(user.getEmail());
           binding.imageProfile.setImageBitmap(getUserImage(user.image));
           binding.getRoot().setOnClickListener(view ->
                   userListener.onUserClicked(user));
       }
    }
    private Bitmap getUserImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
