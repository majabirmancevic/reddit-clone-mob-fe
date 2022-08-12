package com.example.redditapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NavItem implements Parcelable {

    private String mTitle;
    private String mSubtitle;
    private int mIcon;

    public NavItem(String title, String subtitle, int icon) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
    }

    protected NavItem(Parcel in) {
        mTitle = in.readString();
        mSubtitle = in.readString();
        mIcon = in.readInt();
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSubtitle() {
        return mSubtitle;
    }

    public void setmSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mSubtitle);
        out.writeInt(mIcon);
    }
}
