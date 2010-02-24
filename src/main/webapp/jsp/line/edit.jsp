<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- Titre et barre de navigation --%>
<s:url id="urlLigneUpdate" action="edit" namespace="/line">
  <s:param name="idLigne" value="%{id}"/>
</s:url>
<s:if test="id != null">
  <title><s:text name="text.ligne.update.title" /></title>
  <s:property value="filAriane.addElementFilAriane(getText('text.ligne.update.title'), '', #urlLigneUpdate)"/>
</s:if>
<s:else>
  <title><s:text name="text.ligne.create.title" /></title>
  <s:property value="filAriane.addElementFilAriane(getText('text.ligne.create.title'), '', #urlLigneUpdate)"/>
</s:else>
<div class="panelData">
  <s:property value="filAriane.texteFilAriane" escape="false"/>
</div>
<br>
<s:form>
  <s:hidden name="idLigne" value="%{id}"/>
  <s:hidden name="operationMode" value="STORE" />
  <s:hidden key="actionMethod" value="%{actionMethod}"/>
  <%-- Valeur sélectionné par défaut est contenue dans value (chaineIdReseau) et doit être une chaîne de caractère obligatoirement --%>
  <s:select key="idReseau" required="true" name="idReseau" label="%{getText('idReseau')}" value="%{idReseau}" list="reseaux" listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('aucunReseau')}">
  </s:select>
  <%-- Valeur sélectionné par défaut est contenue dans value (chaineIdTransporteur) et doit être une chaîne de caractère obligatoirement --%>
  <s:select key="idTransporteur" required="true" rename="idTransporteur" label="%{getText('idTransporteur')}" value="%{idTransporteur}" list="transporteurs" listKey="id" listValue="name"  headerKey="-1" headerValue="%{getText('aucunTransporteur')}">
  </s:select>
  <s:textfield key="name" required="true"/>
  <s:textfield key="publishedName" />
  <s:textfield key="registrationNumber" required="true"/>
  <s:textfield key="number" />
  <s:if test="id != null">
    <s:select key="transportModeName" list="modesOfTransportEnum" listKey="enumeratedTypeAccess" listValue="textePropriete"/>
  </s:if>
  <s:else>
    <s:select key="transportModeName" list="modesOfTransportEnum" listKey="enumeratedTypeAccess" listValue="textePropriete" value="%{'Bus'}"/>
  </s:else>
  <s:textfield key="comment" name="comment"/>

  <%-- Actions --%>
  <tr>
    <td colspan="2">
      <s:if test="id != null">
        <s:submit key="action.update" action="%{actionMethod}"  theme="simple" cssStyle="float: right;"/>
      </s:if>
      <s:else>
        <s:submit key="action.create" action="%{actionMethod}" theme="simple" cssStyle="float: right;"/>
      </s:else>
      <s:submit key="action.cancel" action="cancel" theme="simple" cssStyle="float: right;"/>
    </td>
  </tr>

  <%-- Ajout des balises tr et td pour le faire apparaitre dans le tableau --%>
  <tr><td colspan="2"><s:include value="/jsp/commun/asterisque.jsp" /></td></tr>
</s:form>