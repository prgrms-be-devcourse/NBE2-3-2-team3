package com.example.bestme.dto.response;

import com.example.bestme.domain.Color;

import java.util.List;

public record ColorMenuResponse(
        List<ColorMenu> colorMenu
) {

    public static ColorMenuResponse from(List<Color> colors) {
        List<ColorMenu> colorMenus = colors.stream()
                .map(ColorMenu::from)
                .toList();

        return new ColorMenuResponse(colorMenus);
    }

    public record ColorMenu(Long id, String name) {

        public static ColorMenu from(Color color) {
            return new ColorMenu(color.getId(), color.getName());
        }
    }
}
