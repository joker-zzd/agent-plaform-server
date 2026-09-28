package com.agent.platform.infrastructure.agent.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * Agent 定义数据访问接口。
 */
public interface AgentDefinitionRepository extends CrudRepository<AgentDefinitionDO, String> {
}
