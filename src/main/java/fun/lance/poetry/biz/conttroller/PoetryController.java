package fun.lance.poetry.biz.conttroller;

import fun.lance.poetry.biz.model.vo.Recommend;
import fun.lance.poetry.biz.service.PoetryBizService;
import fun.lance.poetry.common.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/poetry")
@RequiredArgsConstructor
public class PoetryController {

    private final PoetryBizService poetryBizService;

    @GetMapping("/rec")
    public CommonResult<List<Recommend>> getRecommend() {
        return CommonResult.success(poetryBizService.getRecommends());
    }

}
