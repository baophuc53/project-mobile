package com.ygaps.travelapp.model;

import com.ygaps.travelapp.Object.Member;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchUserResponse implements Serializable {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("users")
    @Expose
    private List<Member> members = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
