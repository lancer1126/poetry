package fun.lance.poetry.biz.conttroller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import fun.lance.poetry.biz.model.dto.SearchDTO;
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

    @GetMapping("/search")
    public CommonResult<Page<PoetryVO>> search(@RequestParam String word,
                                               @RequestParam Integer index,
                                               @RequestParam Integer size) {
        SearchDTO searchDTO = new SearchDTO().setWord(word).setIndex(index).setSize(size);
        return CommonResult.success(poetryBizService.search(searchDTO));
    }

    @PostMapping("/to_meili")
    public CommonResult<Object> uploadToMeili() throws MeilisearchException {
        poetryHandler.uploadToMeili();
        return CommonResult.success();
    }

}
