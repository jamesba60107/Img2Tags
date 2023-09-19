package com.Img2Tags.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageGetTagsApiRequestDTO {

    // 回傳tags的語言 --default: "en"
    private String language;
    // 是否顯示附加信息  --default: 0 (不顯示)
    private Integer verbose;
    // 限制標籤數量 --default：-1 (顯示所有)
    private Integer limit;
    // 最低信心分數限制 --default: 0.0 (顯示大於7信心分數的)
    private Double threshold;
    // 識別出主要物件時 降低父標籤信心分數 信心分數高的較精準--default: 1
    private Integer decrease_parents;
    // 如果有提供, 則進行標籤操作的自定義標籤器ID
    private String tagger_id;

}
