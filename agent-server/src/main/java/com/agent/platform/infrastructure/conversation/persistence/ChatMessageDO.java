package com.agent.platform.infrastructure.conversation.persistence;

import com.agent.platform.infrastructure.persistence.json.JsonbValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 聊天消息数据对象，对应 {@code chat_message} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("chat_message")
public class ChatMessageDO {

    /** 消息唯一标识。 */
    @Id
    @Column("id")
    private UUID id;

    /** 消息所属会话标识。 */
    @Column("session_id")
    private String sessionId;

    /** 产生或消费该消息的执行记录标识。 */
    @Column("execution_id")
    private UUID executionId;

    /** 消息在当前会话中的顺序编号。 */
    @Column("sequence_no")
    private Long sequenceNo;

    /** 消息角色。 */
    @Column("role")
    private String role;

    /** 消息正文。 */
    @Column("content")
    private String content;

    /** 消息内容类型。 */
    @Column("content_type")
    private String contentType;

    /** 消息对应的 Token 数量。 */
    @Column("token_count")
    private Integer tokenCount;

    /** 消息扩展信息，内容为 JSON。 */
    @Column("metadata")
    private JsonbValue metadata;

    /** 创建时间。 */
    @Column("created_time")
    private OffsetDateTime createdTime;
}
