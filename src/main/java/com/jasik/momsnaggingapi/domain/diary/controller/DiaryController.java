package com.jasik.momsnaggingapi.domain.diary.controller;

import com.jasik.momsnaggingapi.domain.diary.Diary;
import com.jasik.momsnaggingapi.domain.diary.Diary.DailyDiary;
import com.jasik.momsnaggingapi.domain.diary.Diary.DailyResponse;
import com.jasik.momsnaggingapi.domain.diary.Diary.DiaryResponse;
import com.jasik.momsnaggingapi.domain.diary.service.DiaryService;
import com.jasik.momsnaggingapi.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diary")
@RequiredArgsConstructor
@Tag(name = "Diary API !!!", description = "일기장 API")
public class DiaryController {

    private final DiaryService diaryService;

    @PutMapping("")
    @Operation(summary = "일기장 수정",
        description = ""
            + "<페이지>\n\n"
            + "홈 → 일기장 → 특정 일 선택 → 작성하기 → 완료\n\n"
            + "홈 → 일기장 → 특정 일 선택 → 오늘의 일기 → 완료\n\n"
            + "홈 → 일기장 → 특정 일 선택 → 삭제\n\n"
            + "<설명>\n\n"
            + "해당 일자의 일기장 내용을 수정합니다.\n\n"
            + "삭제인 경우 title, context 컬럼에 null 대신 '' 값으로 요청합니다.")
    public ResponseEntity<DiaryResponse> putDiary(
        @AuthenticationPrincipal User user,
        final @Valid @RequestBody Diary.DiaryRequest request
    ) {
        Diary.DiaryResponse result = diaryService.putDiary(user.getId(), request);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("")
    @Operation(
        summary = "일기장 조회",
        description = ""
            + "<페이지>\n\n"
            + "홈 → 일기장 → 특정 일 선택\n\n\n"
            + "<설명>\n\n"
            + "해당 일자의 일기장 내용을 조회합니다.\n\n"
            + "today 컬럼을 통해 오늘 날짜 여부를 확인합니다.")
    public ResponseEntity<DiaryResponse> getDiary(
        @AuthenticationPrincipal User user,
        @Schema(description = "일자", example = "2022-04-16", required = true)
        @Parameter(name = "retrieveDate", description = "조회 일자", in = ParameterIn.QUERY) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate retrieveDate
    ) {
        Diary.DiaryResponse result = diaryService.getDiary(user.getId(), retrieveDate);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/calendar")
    @Validated
    @Operation(summary = "월간 달력 일기장 조회", description = ""
        + "<페이지>\n\n"
        + "홈 → 일기장 → 특정 월 선택\n\n\n"
        + "<설명>\n\n"
        + "해당 월의 일별 일기장 작성 여부를 조회합니다.")
    public ResponseEntity<ArrayList<DailyResponse>> getDailyDiaryOfMonth(
        @AuthenticationPrincipal User user,
        @Schema(example = "2022", required = true) @Parameter(name = "retrieveYear", description = "조회할 년도", in = ParameterIn.QUERY) @RequestParam @Min(2022) @Max(2122) int retrieveYear,
        @Schema(example = "05", required = true) @Parameter(name = "retrieveMonth", description = "조회할 월", in = ParameterIn.QUERY) @RequestParam @Min(1) @Max(12) int retrieveMonth) {
        return ResponseEntity.ok().body(diaryService.getDailyDiaryOfMonth(user.getId(), retrieveYear, retrieveMonth));
    }
}
