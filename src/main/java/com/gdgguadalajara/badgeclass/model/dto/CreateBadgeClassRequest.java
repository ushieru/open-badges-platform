package com.gdgguadalajara.badgeclass.model.dto;

public record CreateBadgeClassRequest(
        String name,
        String description,
        String imageBase64,
        String criteriaMd) {

}
