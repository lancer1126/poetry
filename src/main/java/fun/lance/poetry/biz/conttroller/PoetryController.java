package fun.lance.poetry.biz.conttroller;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import fun.lance.poetry.biz.model.vo.Recommend;
import fun.lance.poetry.biz.service.PoetryBizService;
import fun.lance.poetry.common.CommonResult;
import fun.lance.poetry.handler.PoetryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/poetry")
@RequiredArgsConstructor
public class PoetryController {

    private final PoetryBizService poetryBizService;
    private final PoetryHandler poetryHandler;
    private final Client meiliClient;

    @GetMapping("/rec")
    public CommonResult<List<Recommend>> getRecommend() {
        return CommonResult.success(poetryBizService.getRecommends());
    }

    @PostMapping("/to_meili")
    public CommonResult<Object> uploadToMeili() throws MeilisearchException {
        poetryHandler.uploadToMeili();
//        meiliClient.index("poetry_index").deleteAllDocuments();
        return CommonResult.success();
    }

}
