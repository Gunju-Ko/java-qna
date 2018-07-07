package codesquad.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
public class Pages {

    private List<PageInfo> pageInfos;

    private Pages(List<PageInfo> pages) {
        this.pageInfos = pages;
    }

    public static Pages of(Page<?> page, Pageable pageable, String url) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        int start = calculateStart(currentPage, pageSize);
        int end = calculateEnd(start, pageSize, page.getTotalPages());

        return new Pages(makePages(url, start, end, currentPage));
    }

    private static int calculateStart(int currentPage, int pageSize) {
        return (currentPage / pageSize) * pageSize + 1;
    }

    private static int calculateEnd(int start, int pageSize, int totalPages) {
        int end = start + pageSize - 1;
        if (end > totalPages) {
            end = totalPages;
        }
        return end;
    }

    private static List<PageInfo> makePages(String url, int start, int end, int currentPage) {
        return IntStream.rangeClosed(start, end)
                        .mapToObj(pageNumber -> new PageInfo(url, pageNumber, isCurrentPage(pageNumber, currentPage)))
                        .collect(Collectors.toList());
    }

    private static boolean isCurrentPage(int pageNumber, int currentPage) {
        // 현재페이지가 0이면 pageNumber는 1
        return pageNumber == currentPage + 1;
    }
}