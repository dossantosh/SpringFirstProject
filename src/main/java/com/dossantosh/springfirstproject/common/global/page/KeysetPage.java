package com.dossantosh.springfirstproject.common.global.page;

import java.util.List;

public class KeysetPage<T> {
    private List<T> content;
    private boolean hasNext;
    private Long nextId;
    private Long previousId;

    public KeysetPage(List<T> content, Long nextId, Long previousId, boolean hasNext) {
        this.content = content;
        this.nextId = nextId;
        this.previousId = previousId;
        this.hasNext = hasNext;
    }

    public List<T> getContent() { return content; }
    public boolean isHasNext() { return hasNext; }
    public Long getNextId() { return nextId; }
    public Long getPreviousId() { return previousId; }

    public boolean isEmpty() {
        return content == null || content.isEmpty();
    }
}