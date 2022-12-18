package ru.practicum.shareit.item;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> findAllByUserId(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId) {
        return itemService.findAllByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId,
                           @RequestHeader(X_SHARER_USER_ID_HEADER) long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                                @RequestParam String text) {
        return itemService.search(userId, text);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                       @RequestBody @Valid ItemDto itemDto) {
        return itemService.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(itemDto, itemId, userId);
    }
}
