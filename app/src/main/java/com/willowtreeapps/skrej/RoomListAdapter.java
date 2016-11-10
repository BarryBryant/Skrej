//package com.willowtreeapps.skrej;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import android.support.v7.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by chrisestes on 11/10/16.
// */
//
//public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
//
//        public static class ViewHolder extends RecyclerView.ViewHolder {
//
//            public ImageView myView;
//
//            public ViewHolder(View theView) {
//                super(theView);
//
//                myView = (ImageView)theView.findViewById(R.id.poster_image);
//
//            }
//        }
//
//        //Base URL to movie poster image locations.
//        private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
//
//        //Poster image size tag.
//        private final String POSTER_SIZE = "w342";
//
//        //List of movie data.
//        private ArrayList<MovieData> movieList;
//
//        //Application context.
//        private Context appContext;
//
//
//        /** Class constructor ==========================================================================
//         *
//         *  Set context and movie list members.
//         *
//         * @param theAppContext
//         * @param theResources
//         * @param theList
//         *
//         * ------------------------------------------------------------------------------------------ */
//        public PosterImageAdapter(
//                Context                 theAppContext,
//                int                     theResources,
//                ArrayList<MovieData>    theList
//        ) {
//            //Call super.
//
//
//            //Set context and list.
//            this.appContext = theAppContext;
//            this.movieList = theList;
//
//        }
//
//
//
//        @Override
//        public int getItemCount() {
//            return movieList.size();
//        }
//
//        @Override
//        public PosterImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            Context context = parent.getContext();
//            LayoutInflater inflater = LayoutInflater.from(context);
//
//            // Inflate the custom layout
//            View contactView = inflater.inflate(R.layout.poster_image, parent, false);
//
//            // Return a new holder instance
//            PosterImageAdapter.ViewHolder viewHolder = new ViewHolder(contactView);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(PosterImageAdapter.ViewHolder holder, int position) {
//
//
//
//
//            //Get ImageView for current poster to be displayed.
//            ImageView posterImage = holder.myView;  // (ImageView)convertView.findViewById(R.id.poster_image);
//
//            //Make sure we're in bounds.
//            if((movieList != null) && (movieList.size() > position)) {
//
//                //Get full path to poster.
//                String posterPath = new String(IMAGE_BASE_URL + POSTER_SIZE + movieList.get(position).getPosterPath());
//
//                //Load poster image.
//                Picasso.with(appContext)
//                        .load(posterPath)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .into(posterImage);
//            }
//
//
//
//        }
//
//        /** addListPage ================================================================================
//         *
//         *  Add a page of movie data to the list.
//         *
//         * @param PageNum
//         *      Page number of movie data being added.
//         *
//         * @param PageData
//         *      List of movie data to add.
//         *
//         * ------------------------------------------------------------------------------------------ */
//        public void addListPage(
//                int                 PageNum,
//                List<MovieData> PageData
//        ) {
//
//            if(PageData != null) {
//
//                //Add given movie data to our list.
//                this.movieList.addAll(PageData);
//            }
//        }
//
//        public void clear() {
//            this.movieList.clear();
//        }
//
//
//
//    }
//
//}
