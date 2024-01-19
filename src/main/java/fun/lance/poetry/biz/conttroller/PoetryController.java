package fun.lance.poetry.biz.conttroller;

import fun.lance.poetry.biz.model.dto.SearchDTO;
import fun.lance.poetry.biz.model.dto.TranslateDTO;
import fun.lance.poetry.biz.model.vo.PageItem;
import fun.lance.poetry.biz.model.vo.PoetryVO;
import fun.lance.poetry.biz.model.vo.Recommend;
import fun.lance.poetry.biz.service.PoetryBizService;
import fun.lance.poetry.common.CommonResult;
import fun.lance.poetry.handler.PoetryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/poetry")
@RequiredArgsConstructor
public class PoetryController {

    private final PoetryBizService poetryBizService;
    private final PoetryHandler poetryHandler;

    @GetMapping("/rec")
    public CommonResult<List<Recommend>> getRecommend() {
        return CommonResult.success(poetryBizService.getRecommends());
    }

    @PostMapping("/search")
    public CommonResult<PageItem<PoetryVO>> search(@RequestBody SearchDTO searchDTO) {
        return CommonResult.success(poetryBizService.search(searchDTO));
    }

    @PostMapping("/to_meili")
    public CommonResult<Object> uploadToMeili() {
        poetryHandler.uploadToMeili();
        return CommonResult.success();
    }

    @PostMapping("/translate")
    public CommonResult<PoetryVO> translate(@RequestBody TranslateDTO translateDTO) {
        return CommonResult.success(poetryBizService.translate(translateDTO));
    }

}
