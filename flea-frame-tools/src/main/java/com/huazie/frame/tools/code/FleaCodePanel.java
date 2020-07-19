package com.huazie.frame.tools.code;

import com.huazie.frame.common.i18n.FleaI18nHelper;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.db.common.DBSystemEnum;
import com.huazie.frame.tools.common.ToolsConstants;
import com.huazie.frame.tools.common.ToolsException;
import com.huazie.frame.tools.common.ToolsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.GenerationType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 数据操作和业务逻辑层实现 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaCodePanel extends JPanel implements ActionListener, ItemListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleaCodePanel.class);

    private JComboBox<String> dbSystemComboBox; // 数据库系统下拉框

    private JTextField dbNameTextField; // 数据库名

    private JTextField tableNameTextField; // 表名

    private JTextField tableNameDescTextField; // 表名描述

    private JComboBox<String> idGeneratorStrategyComboBox; // 主键生成策略下拉框

    private JLabel idGeneratorTableLabel; // 主键生成器表标签

    private JTextField idGeneratorTableTextField; // 主键生成器表文本域

    private JLabel pkColumnNameLabel; // 主键列名

    private JTextField pkColumnNameTextField; // 主键列名文本域

    private JLabel valueColumnNameLabel; // 主键值列名

    private JTextField valueColumnNameTextField; // 主键值列名文本域

    private JTextField authorTextField; // 作者

    private JTextField versionTextField; // 版本

    private JTextField rootPathTextField; // 根目录

    private JButton rootPathSelectButton; // 根目录选择按钮

    private JTextField codePackageTextField; // 包名

    private JRadioButton newRadioButton; // 新建持久化单元DAO层实现 单选按钮

    private JRadioButton oldRadioButton; // 现有持久化单元DAO层实现 单选按钮

    private ButtonGroup btnGroup; // 单选按钮组

    private JTextField puDaoPackageTextField; // 持久化单元DAO层实现包名

    private JLabel puDAOClassNameLabel;

    private JTextField puDaoClassNameTextField; // 持久化单元DAO层实现类名

    private JLabel persistenceUnitNameLabel;

    private JTextField puNameTextField; // 持久化单元名

    private JLabel persistenceUnitAliasNameLabel;

    private JTextField puAliasNameTextField; // 持久化单元别名

    private JButton importButton; // 导入 按钮

    private JButton generateButton; // 生成 按钮

    private JButton destroyButton; // 销毁 按钮

    private JButton resetButton; // 重置 按钮

    private GridBagLayout configGridBagLayout;

    private JPanel configPanel;

    private GridBagConstraints configGridBagConstraints;

    public FleaCodePanel() {
        init();
    }

    private void init() {

        setLayout(new GridLayout(1, 1));

        configGridBagLayout = new GridBagLayout();
        configPanel = new JPanel(configGridBagLayout);
        configGridBagConstraints = new GridBagConstraints();
        configGridBagConstraints.fill = GridBagConstraints.BOTH;

        initElements(); // 初始化页面元素

        add(configPanel);
    }

    private void initElements() {
        // 初始化数据库配置页面元素
        initDBConfig();

        // 添加分隔线
        addSeparator();

        // 初始化主键生成策略配置页面元素
        initIDGeneratorConfig();

        // 添加分隔线
        addSeparator();

        // 初始化代码配置页面元素
        initCodeConfig();

        // 添加分隔线
        addSeparator();

        // 初始化持久化单元配置页面元素
        initPersistenceUnitConfig();

        // 添加分隔线
        addSeparator();

        // 初始化按钮
        initButton();
    }

    private void initDBConfig() {
        // 数据库配置
        showTitleConfig("COMMON_CODE_00001");

        configGridBagConstraints.insets = new Insets(2, 0, 2, 0);
        configGridBagConstraints.weightx = 0.0;

        // 数据库系统下拉框
        dbSystemComboBox = new JComboBox<String>();
        dbSystemComboBox.addItem(DBSystemEnum.MySQL.getName());
        dbSystemComboBox.addItem(DBSystemEnum.Oracle.getName());

        // 数据库系统
        showOneContentConfig(null, "COMMON_CODE_00002", dbSystemComboBox, 4, false);

        // 数据库名文本域
        dbNameTextField = new JTextField();
        // 数据库名
        showOneContentConfig(null, "COMMON_CODE_00003", dbNameTextField, 4, true);

        // 表名文本域
        tableNameTextField = new JTextField();
        // 表名
        showOneContentConfig(null, "COMMON_CODE_00004", tableNameTextField, 4, false);

        // 表名描述文本域
        tableNameDescTextField = new JTextField();
        // 表名描述
        showOneContentConfig(null, "COMMON_CODE_00005", tableNameDescTextField, 4, true);
    }

    private void initIDGeneratorConfig() {
        // 主键生成策略配置
        showTitleConfig("COMMON_CODE_00021");

        // 主键生成策略下拉框
        idGeneratorStrategyComboBox = new JComboBox<String>();
        idGeneratorStrategyComboBox.addItem(GenerationType.TABLE.name());
        idGeneratorStrategyComboBox.addItem(GenerationType.IDENTITY.name());
        idGeneratorStrategyComboBox.addItem(GenerationType.SEQUENCE.name());
        idGeneratorStrategyComboBox.addItemListener(this);
        // 主键生成策略
        showOneContentConfig(null, "COMMON_CODE_00022", idGeneratorStrategyComboBox, 4, false);

        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        configGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // end row
        addEmptyLabel();

        // 主键生成器表标签
        idGeneratorTableLabel = new JLabel(FleaI18nHelper.i18nForCommon("COMMON_CODE_00023") + ":", JLabel.RIGHT);
        // 主键生成器表文本域
        idGeneratorTableTextField = new JTextField();
        // 主键生成策略
        showOneContentConfig(idGeneratorTableLabel, "", idGeneratorTableTextField, 3, false);

        // 主键列名标签
        pkColumnNameLabel = new JLabel(FleaI18nHelper.i18nForCommon("COMMON_CODE_00024") + ":", JLabel.RIGHT);
        // 主键列名文本域
        pkColumnNameTextField = new JTextField();
        showOneContentConfig(pkColumnNameLabel, "", pkColumnNameTextField, 3, false);

        // 主键值列名标签
        valueColumnNameLabel = new JLabel(FleaI18nHelper.i18nForCommon("COMMON_CODE_00025") + ":", JLabel.RIGHT);
        // 主键值列名文本域
        valueColumnNameTextField = new JTextField();
        showOneContentConfig(valueColumnNameLabel, "", valueColumnNameTextField, 0, true);

    }

    private void initCodeConfig() {
        // 代码配置
        showTitleConfig("COMMON_CODE_00006");

        // 作者文本域
        authorTextField = new JTextField();
        // 作者
        showOneContentConfig(null, "COMMON_CODE_00007", authorTextField, 4, false);

        // 版本文本域
        versionTextField = new JTextField();
        // 版本
        showOneContentConfig(null, "COMMON_CODE_00008", versionTextField, 4, true);

        // 根目录文本域
        rootPathTextField = new JTextField();
        // 根目录
        showOneContentConfig(null, "COMMON_CODE_00009", rootPathTextField, 9, false);

        // 根目录选择按钮
        rootPathSelectButton = new JButton(FleaI18nHelper.i18nForCommon("COMMON_I18N_00008"));
        rootPathSelectButton.addActionListener(this);
        configGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // end row
        configGridBagLayout.setConstraints(rootPathSelectButton, configGridBagConstraints);
        configPanel.add(rootPathSelectButton);

        // 包名文本域
        codePackageTextField = new JTextField();
        // 包名
        showOneContentConfig(null, "COMMON_CODE_00010", codePackageTextField, 0, true);
    }

    private void initPersistenceUnitConfig() {
        // 持久化单元配置
        showTitleConfig("COMMON_CODE_00020");

        // 持久化单元DAO层实现 单元框， 新建 或 现有
        JLabel puDaoClassLabel = new JLabel(FleaI18nHelper.i18nForCommon("COMMON_CODE_00011") + ":", JLabel.RIGHT);
        puDaoClassLabel.setFont(new Font("宋体", Font.PLAIN, 15)); // 字体
        // 新建 持久化单元DAO层实现类
        newRadioButton = new JRadioButton(FleaI18nHelper.i18nForCommon("COMMON_CODE_00012"));
        newRadioButton.addActionListener(this); // 添加按钮事件
        // 现有 持久化单元DAO层实现类
        oldRadioButton = new JRadioButton(FleaI18nHelper.i18nForCommon("COMMON_CODE_00013"));
        oldRadioButton.addActionListener(this); // 添加按钮事件
        // 创建按钮组，把两个单选按钮添加到该组
        btnGroup = new ButtonGroup();
        btnGroup.add(newRadioButton);
        btnGroup.add(oldRadioButton);

        configGridBagConstraints.gridwidth = 1;
        configGridBagLayout.setConstraints(puDaoClassLabel, configGridBagConstraints);
        configPanel.add(puDaoClassLabel);
        configGridBagConstraints.gridwidth = 3;
        configGridBagLayout.setConstraints(newRadioButton, configGridBagConstraints);
        configPanel.add(newRadioButton);
        configGridBagConstraints.gridwidth = 3;
        configGridBagLayout.setConstraints(oldRadioButton, configGridBagConstraints);
        configPanel.add(oldRadioButton);
        configGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // end row
        JLabel radioLabel = new JLabel(" ");
        configGridBagLayout.setConstraints(radioLabel, configGridBagConstraints);
        configPanel.add(radioLabel);

        // 持久化单元DAO层实现类包名文本域
        puDaoPackageTextField = new JTextField();
        // 持久化单元DAO层实现类包名
        showOneContentConfig(null, "COMMON_CODE_00010", puDaoPackageTextField, 0, true);

        // 持久化单元DAO层实现类名
        puDAOClassNameLabel = new JLabel(FleaI18nHelper.i18nForCommon("COMMON_CODE_00016") + ":", JLabel.RIGHT);
        // 持久化单元DAO层实现类名文本域
        puDaoClassNameTextField = new JTextField();
        showOneContentConfig(puDAOClassNameLabel, "", puDaoClassNameTextField, 0, true);

        // 持久化单元名
        persistenceUnitNameLabel = new JLabel(FleaI18nHelper.i18nForCommon("COMMON_CODE_00014") + ":", JLabel.RIGHT);
        // 持久化单元名文本域
        puNameTextField = new JTextField();
        showOneContentConfig(persistenceUnitNameLabel, "", puNameTextField, 4, false);

        // 持久化单元别名 （单词首字母大写）
        persistenceUnitAliasNameLabel = new JLabel(FleaI18nHelper.i18nForCommon("COMMON_CODE_00015") + ":", JLabel.RIGHT);
        // 持久化单元别名文本域
        puAliasNameTextField = new JTextField();
        showOneContentConfig(persistenceUnitAliasNameLabel, "", puAliasNameTextField, 0, true);

        // 初始化
        // 设置第一个单选按钮选中
        oldRadioButton.setSelected(true);
        persistenceUnitNameLabel.setVisible(false);
        puNameTextField.setVisible(false);
        persistenceUnitAliasNameLabel.setVisible(false);
        puAliasNameTextField.setVisible(false);
    }

    /**
     * <p> 处理配置标题展示 </p>
     *
     * @param titleNameKey 标题配置标签名称i18n key
     * @since 1.0.0
     */
    private void showTitleConfig(String titleNameKey) {

        JLabel configTitleLabel = new JLabel(FleaI18nHelper.i18nForCommon(titleNameKey), JLabel.LEFT);
        configTitleLabel.setFont(new Font("宋体", Font.PLAIN, 20)); // 字体

        configGridBagConstraints.weightx = 1.0;
        configGridBagConstraints.gridwidth = 1;
        configGridBagConstraints.insets = new Insets(5, 20, 2, 0);
        configGridBagLayout.setConstraints(configTitleLabel, configGridBagConstraints);
        configPanel.add(configTitleLabel);
        configGridBagConstraints.insets = new Insets(5, 5, 2, 0);
        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        addEmptyLabel();
        configGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // end row
        addEmptyLabel();
    }

    /**
     * <p> 添加一个空标签【占位】 </p>
     */
    private void addEmptyLabel() {
        JLabel other = new JLabel(" ");
        configGridBagLayout.setConstraints(other, configGridBagConstraints);
        configPanel.add(other);
    }

    /**
     * <p> 展示一个内容配置  </p>
     *
     * @param labelNameKey 内容配置标签名称i18n key
     * @since 1.0.0
     */
    private void showOneContentConfig(JComponent labelJComponent, String labelNameKey, JComponent contentJComponent, int contentGridWidth, boolean isNewLine) {
        if (null == labelJComponent) {
            labelJComponent = new JLabel(FleaI18nHelper.i18nForCommon(labelNameKey) + ":", JLabel.RIGHT);
        }
        labelJComponent.setFont(new Font("宋体", Font.PLAIN, 15)); // 字体

        configGridBagConstraints.gridwidth = 1;
        configGridBagLayout.setConstraints(labelJComponent, configGridBagConstraints);
        configPanel.add(labelJComponent);
        if (isNewLine) {
            configGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // end row
        } else {
            configGridBagConstraints.gridwidth = contentGridWidth;
        }
        configGridBagLayout.setConstraints(contentJComponent, configGridBagConstraints);
        configPanel.add(contentJComponent);
    }

    private void addSeparator() {
        configGridBagConstraints.weightx = 0.0;
        configGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // end row
        configGridBagConstraints.insets = new Insets(2, 0, 2, 0);
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setBackground(new Color(10, 10, 10));
        configGridBagLayout.setConstraints(sep, configGridBagConstraints);
        configPanel.add(sep);
    }

    private void initButton() {
        JPanel buttonPanel = new JPanel();
        // 生成按钮
        generateButton = new JButton(FleaI18nHelper.i18nForCommon("COMMON_CODE_00017"));
        generateButton.addActionListener(this);
        buttonPanel.add(generateButton);

        // 销毁按钮
        destroyButton = new JButton(FleaI18nHelper.i18nForCommon("COMMON_CODE_00018"));
        destroyButton.addActionListener(this);
        buttonPanel.add(destroyButton);

        resetButton = new JButton(FleaI18nHelper.i18nForCommon("COMMON_I18N_00007"));
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);

        // 导入按钮
        importButton = new JButton(FleaI18nHelper.i18nForCommon("COMMON_CODE_00019"));
        importButton.addActionListener(this);
        buttonPanel.add(importButton);

        configGridBagConstraints.weightx = 0.0;
        configGridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // end row
        configGridBagConstraints.insets = new Insets(2, 0, 2, 0);
        configGridBagLayout.setConstraints(buttonPanel, configGridBagConstraints);
        configPanel.add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            Object source = ae.getSource();
            if (newRadioButton == source) {
                puDAOClassNameLabel.setVisible(false);
                puDaoClassNameTextField.setVisible(false);
                persistenceUnitNameLabel.setVisible(true);
                puNameTextField.setVisible(true);
                persistenceUnitAliasNameLabel.setVisible(true);
                puAliasNameTextField.setVisible(true);
            } else if (oldRadioButton == source) {
                puDAOClassNameLabel.setVisible(true);
                puDaoClassNameTextField.setVisible(true);
                persistenceUnitNameLabel.setVisible(false);
                puNameTextField.setVisible(false);
                persistenceUnitAliasNameLabel.setVisible(false);
                puAliasNameTextField.setVisible(false);
            } else if (generateButton == source) {
                code();
            } else if (destroyButton == source) {
                clean();
            } else if (resetButton == source) {
                reset();
            } else if (rootPathSelectButton == source) {
                selectFile();
            } else if (importButton == source) {
                importConfig();
            }
        } catch (Exception e1) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Exception = {}", e1);
            }
            ToolsHelper.showTips(FleaI18nHelper.i18nForCommon("COMMON_I18N_00010"), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        try {
            if (ItemEvent.DESELECTED == e.getStateChange()) {
                return;
            }
            Object source = e.getSource();
            if (idGeneratorStrategyComboBox == source) {
                Object dbSystemObj = dbSystemComboBox.getSelectedItem();
                Object idGeneratorStrategyObj = idGeneratorStrategyComboBox.getSelectedItem();
                if (ObjectUtils.isNotEmpty(dbSystemObj) && ObjectUtils.isNotEmpty(idGeneratorStrategyObj)) {
                    String dbSystemName = dbSystemObj.toString();
                    String idGeneratorStrategy = idGeneratorStrategyObj.toString();
                    if (GenerationType.TABLE.name().equals(idGeneratorStrategy)) {
                        setGenerationTypeTableVisible(true);
                    } else if (GenerationType.IDENTITY.name().equals(idGeneratorStrategy)) {
                        if (DBSystemEnum.Oracle.getName().equals(dbSystemName)) {
                            // Oracle不支持GenerationType.IDENTITY
                            throw new Exception(FleaI18nHelper.i18nForCommon("COMMON_I18N_00017"));
                        }
                        setGenerationTypeTableVisible(false);
                    } else if (GenerationType.SEQUENCE.name().equals(idGeneratorStrategy)) {
                        if (DBSystemEnum.MySQL.getName().equals(dbSystemName)) {
                            // MySQL不支持GenerationType.SEQUENCE
                            throw new Exception(FleaI18nHelper.i18nForCommon("COMMON_I18N_00018"));
                        }
                        setGenerationTypeTableVisible(false);
                    }
                }
            }
        } catch (Exception e1) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Exception = {}", e1);
            }
            ToolsHelper.showTips(FleaI18nHelper.i18nForCommon("COMMON_I18N_00010"), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
            idGeneratorStrategyComboBox.setSelectedIndex(0); // 重新选择TABLE
        }
    }

    private void setGenerationTypeTableVisible(boolean isShow) {
        idGeneratorTableLabel.setVisible(isShow);
        idGeneratorTableTextField.setVisible(isShow);
        pkColumnNameLabel.setVisible(isShow);
        pkColumnNameTextField.setVisible(isShow);
        valueColumnNameLabel.setVisible(isShow);
        valueColumnNameTextField.setVisible(isShow);
    }

    private void code() throws Exception {
        // 编写代码
        FleaCodeProgrammer.code(createParamMap());
    }

    private void clean() throws Exception {
        // 清理代码
        FleaCodeProgrammer.clean(createParamMap());
    }

    /**
     * <p> 重置 </p>
     *
     * @since 1.0.0
     */
    private void reset() {
        // 数据库配置
        resetDBConfig();

        // 主键生成策略配置
        resetIDGeneratorStrategyConfig();

        // 代码配置
        resetCodeConfig();

        // 持久化单元配置
        resetPersistenceUnitConfig();
    }

    private void resetDBConfig() {
        dbSystemComboBox.setSelectedIndex(0);
        dbNameTextField.setText("");
        tableNameTextField.setText("");
        tableNameDescTextField.setText("");
    }

    private void resetIDGeneratorStrategyConfig() {
        idGeneratorStrategyComboBox.setSelectedIndex(0);
        setGenerationTypeTableVisible(true);
        idGeneratorTableTextField.setText("");
        pkColumnNameTextField.setText("");
        valueColumnNameTextField.setText("");
    }

    private void resetCodeConfig() {
        authorTextField.setText("");
        versionTextField.setText("");
        rootPathTextField.setText("");
        codePackageTextField.setText("");
    }

    private void resetPersistenceUnitConfig() {
        puDaoPackageTextField.setText("");
        puDaoClassNameTextField.setText("");
        puNameTextField.setText("");
        puAliasNameTextField.setText("");
    }

    /**
     * <p> 导入配置 </p>
     *
     * @since 1.0.0
     */
    private void importConfig() {
        FleaCodeConfig config = FleaCodeConfig.getConfig();
        dbNameTextField.setText(config.getDbName());
        tableNameTextField.setText(config.getTableName());
        tableNameDescTextField.setText(config.getTableNameDesc());
        idGeneratorTableTextField.setText(config.getIdGeneratorTable());
        pkColumnNameTextField.setText(config.getPkColumnName());
        valueColumnNameTextField.setText(config.getValueColumnName());
        authorTextField.setText(config.getAuthor());
        versionTextField.setText(config.getVersion());
        rootPathTextField.setText(config.getRootPath());
        codePackageTextField.setText(config.getCodePackage());
        puDaoPackageTextField.setText(config.getPuDaoCodePackage());
        puDaoClassNameTextField.setText(config.getPuDaoClassName());
        puNameTextField.setText(config.getPuName());
        puAliasNameTextField.setText(config.getPuAliasName());
    }

    /**
     * <p> 弹出选择文件页面 </p>
     *
     * @since 1.0.0
     */
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.showDialog(new JLabel(), FleaI18nHelper.i18nForCommon("COMMON_I18N_00008"));
        File file = fileChooser.getSelectedFile();
        if (ObjectUtils.isNotEmpty(file)) {
            rootPathTextField.setText(file.getAbsolutePath());
        }
    }

    private Map<String, Object> createParamMap() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        // 1. 数据库配置
        // 获取数据库系统名
        String dbSystemName = StringUtils.valueOf(dbSystemComboBox.getSelectedItem());
        param.put(ToolsConstants.CodeConstants.DB_SYSTEM_NAME, dbSystemName);
        // 获取数据库名
        String dbName = dbNameTextField.getText();
        if (StringUtils.isBlank(dbName)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00003"));
        }
        param.put(ToolsConstants.CodeConstants.DB_NAME, dbName);
        // 获取表名
        String tableName = tableNameTextField.getText();
        if (StringUtils.isBlank(tableName)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00004"));
        }
        param.put(ToolsConstants.CodeConstants.TABLE_NAME, tableName);
        // 获取表名描述
        String tableNameDesc = tableNameDescTextField.getText();
        if (StringUtils.isBlank(tableNameDesc)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00005"));
        }
        param.put(ToolsConstants.CodeConstants.TABLE_DESC, tableNameDesc);

        // 2主键生成策略配置
        // 获取主键生成策略
        String idGeneratorStrategy = StringUtils.valueOf(idGeneratorStrategyComboBox.getSelectedItem());
        param.put(ToolsConstants.CodeConstants.ID_GENERATOR_STRATEGY, idGeneratorStrategy);
        // 获取主键生成器表
        String idGeneratorTable = idGeneratorTableTextField.getText();
        if (StringUtils.isNotBlank(idGeneratorTable)) {
            param.put(ToolsConstants.CodeConstants.ID_GENERATOR_TABLE, idGeneratorTable);
        }
        // 获取主键列名
        String pkColumnName = pkColumnNameTextField.getText();
        if (StringUtils.isNotBlank(pkColumnName)) {
            param.put(ToolsConstants.CodeConstants.PK_COLUMN_NAME, pkColumnName);
        }
        // 获取主键值列名
        String valueColumnName = valueColumnNameTextField.getText();
        if (StringUtils.isNotBlank(valueColumnName)) {
            param.put(ToolsConstants.CodeConstants.VALUE_COLUMN_NAME, valueColumnName);
        }

        // 3. 代码配置
        // 获取作者
        String author = authorTextField.getText();
        if (StringUtils.isBlank(author)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00007"));
        }
        param.put(ToolsConstants.CodeConstants.AUTHOR, author);
        // 获取版本
        String version = versionTextField.getText();
        if (StringUtils.isBlank(version)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00008"));
        }
        param.put(ToolsConstants.CodeConstants.VERSION, version);
        // 获取根目录
        String rootPath = rootPathTextField.getText();
        if (StringUtils.isBlank(rootPath)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00009"));
        }
        param.put(ToolsConstants.CodeConstants.ROOT_PACKAGE, rootPath);
        // 获取代码包名
        String codePackage = codePackageTextField.getText();
        if (StringUtils.isBlank(codePackage)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00010"));
        }
        param.put(ToolsConstants.CodeConstants.CODE_PACKAGE, codePackage);

        // 4. 持久化单元配置
        String puDaoClassPackage = puDaoPackageTextField.getText();
        if (StringUtils.isBlank(puDaoClassPackage)) {
            throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00011") + FleaI18nHelper.i18nForCommon("COMMON_CODE_00010"));
        }
        param.put(ToolsConstants.CodeConstants.FLEA_PERSISTENCE_UNIT_DAO_CLASS_PACKAGE, puDaoClassPackage);
        // 获取单选按钮
        if (btnGroup.isSelected(newRadioButton.getModel())) { // 选择新建
            // 持久化单元名
            String fleaPersistenceUnitName = puNameTextField.getText();
            if (StringUtils.isBlank(fleaPersistenceUnitName)) {
                throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00014"));
            }
            param.put(ToolsConstants.CodeConstants.FLEA_PERSISTENCE_UNIT_NAME, fleaPersistenceUnitName);
            // 持久化单元别名
            String fleaPersistenceUnitAliasName = puAliasNameTextField.getText();
            if (StringUtils.isBlank(fleaPersistenceUnitAliasName)) {
                throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00015"));
            }
            param.put(ToolsConstants.CodeConstants.FLEA_PERSISTENCE_UNIT_ALIAS_NAME, fleaPersistenceUnitAliasName);
        } else { // 选择现有
            // 持久化单元DAO层实现类名
            String fleaPersistenceUnitDaoClassName = puDaoClassNameTextField.getText();
            if (StringUtils.isBlank(fleaPersistenceUnitDaoClassName)) {
                throw new ToolsException("COMMON_I18N_00013", FleaI18nHelper.i18nForCommon("COMMON_CODE_00016"));
            }
            param.put(ToolsConstants.CodeConstants.FLEA_PERSISTENCE_UNIT_DAO_CLASS_NAME, fleaPersistenceUnitDaoClassName);
        }

        return param;
    }

}
