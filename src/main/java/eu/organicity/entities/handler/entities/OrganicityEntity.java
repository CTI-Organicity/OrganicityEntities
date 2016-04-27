package eu.organicity.entities.handler.entities;

import com.amaxilatis.orion.OrionClient;
import com.amaxilatis.orion.model.OrionContextElement;
import eu.organicity.entities.handler.attributes.Attribute;
import eu.organicity.entities.namespace.OrganicityEntityTypes;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by etheodor on 20/10/2015.
 */
public class OrganicityEntity {

    final List<Attribute> attributes = new ArrayList<>();
    String id;
    OrganicityEntityTypes.EntityType entityType;
    private Date date;
    private Double latitude; // Encoded in WGS84
    private Double longitude; // Encoded in WGS84
    private String area; //Encoded in GeoJSON

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public OrganicityEntity(OrganicityEntityTypes.EntityType entityType) {
        this.id = null;
        this.entityType = entityType;
        this.date = null;
        this.latitude = null;
        this.longitude = null;
        this.area = null;
    }

    public OrganicityEntity(String id, OrganicityEntityTypes.EntityType entityType) {
        this.id = id;
        this.entityType = entityType;
        this.date = null;
        this.latitude = null;
        this.longitude = null;
        this.area = null;
    }

    public OrionContextElement getContextElement() {
        OrionContextElement element = new OrionContextElement();
        element.setId(id);
        element.setType(entityType.getUrn());

        for (Attribute attribute : attributes) {
            element.getAttributes().add(attribute.getAttribute());
        }
        if (date != null) {
            element.getAttributes().add(OrionClient.createAttribute("TimeInstant", "ISO8601", df.format(date)));
        }
        if (latitude != null && longitude != null) {
            element.getAttributes().add(OrionClient.createAttributeWithMetadata("position", "coords", latitude + ", " + longitude, "location", "string", "WGS84"));
        }

        if (area != null) {
            area = URLEncoder.encode(area);
            if (area.length() > 32 * 1024) {
                // area="";
            }
            element.getAttributes().add(OrionClient.createAttributeWithMetadata("area", "string", area, "mediatype", "string", "GeoJson"));
        }

        return element;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setTimestamp(final Date date) {
        this.date = date;
    }

    public void setPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setArea(String area) {
        this.area = area; //todo: possible add GeoJson Validations
    }

    public void addAttribute(Attribute a) {
        attributes.add(a);
    }


    @Override
    public String toString() {
        return "OrganicityEntity{" +
                "attributes=" + attributes +
                ", id='" + id + '\'' +
                ", entityType=" + entityType +
                '}';
    }

}
