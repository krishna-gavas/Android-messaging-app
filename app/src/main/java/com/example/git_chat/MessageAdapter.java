package com.example.git_chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mMsgRef;
    private FirebaseAuth mAuth;

    private LinearLayout layout;


    public  TextView messageText;
    public CircleImageView profileImage;
    public TextView displayName;
    public ImageView messageImage;



    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout_left,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

//        public  TextView messageText;
        public CircleImageView profileImage;
//        public TextView displayName;
//        public ImageView messageImage;

        public LinearLayout leftMsgLayout;

        public LinearLayout rightMsgLayout;

        public TextView leftMsgTextView;

        public TextView rightMsgTextView;

        public TextView fromTime;

        public TextView toTime;



        public MessageViewHolder(View view) {
            super(view);

//            messageText = (TextView) view.findViewById(R.id.message_text_layout);
//            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
////            displayName = (TextView) view.findViewById(R.id.name_text_layout);
//            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
//
//            layout = view.findViewById(R.id.message_single_layout);

            leftMsgLayout =  itemView.findViewById(R.id.chat_left_msg_layout);
            rightMsgLayout =  itemView.findViewById(R.id.chat_right_msg_layout);
            leftMsgTextView =  itemView.findViewById(R.id.chat_left_msg_text_view);
            rightMsgTextView =  itemView.findViewById(R.id.chat_right_msg_text_view);
            fromTime = itemView.findViewById(R.id.from_time);
            toTime = itemView.findViewById(R.id.to_time);



        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int position) {

        mAuth = FirebaseAuth.getInstance();

        final String current_user_id = mAuth.getCurrentUser().getUid();

        final Messages msgDto = mMessageList.get(position);

        String from_user = msgDto.getFrom();
        final String message_type = msgDto.getType();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mMsgRef = FirebaseDatabase.getInstance().getReference().child("messages");

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();


                if(dataSnapshot.getKey().equals(current_user_id))
                {

                    viewHolder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);

                    if(message_type.equals("text")) {

                        viewHolder.rightMsgTextView.setText(msgDto.getMessage());

                    }
                    viewHolder.leftMsgLayout.setVisibility(LinearLayout.GONE);
                }
                else
                {
                    viewHolder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);

                    if(message_type.equals("text")) {

                        viewHolder.leftMsgTextView.setText(msgDto.getMessage());

                    }
                    viewHolder.rightMsgLayout.setVisibility(LinearLayout.GONE);
                }



//                viewHolder.displayName.setText(name);

//                Picasso.with(viewHolder.profileImage.getContext()).load(image)
//                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        if(message_type.equals("text")) {
//
//            viewHolder.messageText.setText(c.getMessage());
//            viewHolder.messageImage.setVisibility(View.INVISIBLE);
//
//
//        } else {
//
//            viewHolder.messageText.setVisibility(View.INVISIBLE);
//            Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
//                    .placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);
//
//        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

}
