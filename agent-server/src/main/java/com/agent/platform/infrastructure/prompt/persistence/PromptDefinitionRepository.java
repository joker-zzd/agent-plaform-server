package com.agent.platform.infrastructure.prompt.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * Prompt 定义数据访问接口。
 */
public interface PromptDefinitionRepository extends CrudRepository<PromptDefinitionDO, String> {
}
