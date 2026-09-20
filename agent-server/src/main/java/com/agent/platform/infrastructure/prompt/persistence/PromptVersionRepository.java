package com.agent.platform.infrastructure.prompt.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Prompt 版本数据访问接口。
 */
public interface PromptVersionRepository extends CrudRepository<PromptVersionDO, UUID> {

    /**
     * 根据 Prompt 标识和版本号查询唯一版本。
     *
     * @param promptId Prompt 唯一标识
     * @param version Prompt 版本号
     * @return Prompt 版本数据对象
     */
    Optional<PromptVersionDO> findByPromptIdAndVersion(String promptId, Integer version);
}
