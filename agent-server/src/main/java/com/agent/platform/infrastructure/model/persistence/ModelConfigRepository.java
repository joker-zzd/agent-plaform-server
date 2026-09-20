package com.agent.platform.infrastructure.model.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * 模型配置数据访问接口。
 */
public interface ModelConfigRepository extends CrudRepository<ModelConfigDO, String> {
}
