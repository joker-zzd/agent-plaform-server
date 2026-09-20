package com.agent.platform.infrastructure.conversation.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

/**
 * 聊天会话数据对象，对应 {@code chat_session} 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("chat_session")
public class ChatSessionDO {

    /** 会话唯一标识。 */
    @Id
    @Column("id")
    private String id;

    /** 当前会话使用的 Agent 标识。 */
    @Column("agent_id")
    private String agentId;

    /** 会话所属用户标识。 */
    @Column("user_id")
    private String userId;

    /** 会话标题。 */
    @Column("title")
    private String title;

    /** 会话状态。 */
    @Column("status")
    private String status;

    /** 会话扩展信息，内容为 JSON。 */
    @Column("metadata")
    private String metadata;

    /** 最后一条消息的产生时间。 */
    @Column("last_message_time")
    private OffsetDateTime lastMessageTime;

    /** 创建时间。 */
    @Column("created_time")
    private OffsetDateTime createdTime;

    /** 最后更新时间。 */
    @Column("updated_time")
    private OffsetDateTime updatedTime;
}
